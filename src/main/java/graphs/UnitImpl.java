package graphs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

public class UnitImpl implements Unit{

    private static Logger LOGGER =  LoggerFactory.getLogger("UnitImpl");

    private Graph<Unit> graph;

    private UnitDelegate delegate;

    private String name;

    private Double state;

    public UnitImpl(Graph<Unit> graph, String nodeName, Double state){
        if(graph==null){
            throw new RuntimeException("Graph must be valid");
        }
        boolean isValidState = graph.getNewStateIsAcceptablePredicate().test(state);
        if(!isValidState){
            throw new RuntimeException("You did not provide a correct state");
        }
        this.state = state;
        this.name = nodeName;
        this.graph = graph;
    }

    public void setDelegate(UnitDelegate delegate) {
        this.delegate = delegate;
    }

    public UnitImpl(String nodeName, double state){
        Predicate<Double> predicate = graph!=null ? graph.getNewStateIsAcceptablePredicate() : GraphImpl.DEFAULT_PREDICATE;
        boolean isValidState = predicate.test(state);
        if(!isValidState){
            throw new RuntimeException("You did not provide a correct state");
        }
        this.state = state;
        this.name = nodeName;
    }

    @Override
    public Graph<Unit> graph() {
        return graph;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if( this==obj ){
            return true;
        }
        if( obj==null || !(obj instanceof UnitImpl) ){
            return false;
        }
        UnitImpl other = (UnitImpl) obj;
        return this.name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public void sendUpdate(UnitMessage message) {
        graph.deliverMessage(this, message);
    }

    @Override
    public double getState() {
        return state;
    }

    @Override
    public void setGraph(Graph graph) {
        if(this.graph!=null){
            return;
        }
        this.graph = graph;
    }

    @Override
    public void setState(Double newState) {
        if( !graph.getNewStateIsAcceptablePredicate().test(newState) ){
            throw new InvalidNewStateException(this, newState);
        }
        LOGGER.info("Setting state to: "+newState+", old state was: "+state);
        Double oldState = this.state;
        this.state = newState;
        sendUpdate( new UpdateStateMessage(this, oldState, newState) );
    }

    @Override
    public String toString() {
        return String.format("[Node] Name='%s', State='%s'", getName(), getState());
    }

    @Override
    public void receiveMessage(UnitMessage message) {
        if(delegate!=null){
            delegate.updatedReceived(this, message);
        }
        if(message instanceof  UpdateStateMessage){
            UpdateStateMessage updateStateMessage =(UpdateStateMessage) message;
            double actualUpdate = updateStateMessage.getUpdateDifference()* updateStateMessage.getConnectionWeight();
            LOGGER.info("Updating state, old is: "+ this.state+" new is: "+ this.state+actualUpdate);
            double oldState = this.state;
            this.state+=actualUpdate;
            if( !GraphUtility.isCloseEnough(oldState, this.state) ){
                sendUpdate(new UpdateStateMessage(this, oldState, this.state));
            }
        }
    }
}
