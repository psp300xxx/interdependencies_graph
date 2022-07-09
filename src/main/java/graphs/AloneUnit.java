package graphs;

import graphs.publish_subscribe.UnitDelegate;
import graphs.publish_subscribe.UnitManagerThread;
import graphs.publish_subscribe.UnitMessage;
import graphs.publish_subscribe.UnitMessageImpl;
import org.json.JSONArray;
import org.json.JSONObject;
import utility.ProjectUtility;

import java.util.*;

public class AloneUnit implements Unit{

    private double state;

    private UnitDelegate delegate;

    private UnitManagerThread unitManagerThread;
    private String name;

    private List<Unit> outboundingConnections;

    private List<Unit> inboundingConnections;

    public AloneUnit(String name){
        this.name = name;
    }

    @Override
    public UnitManagerThread getThread() {
        return unitManagerThread;
    }

    @Override
    public UnitDelegate getDelegate() {
        return delegate;
    }

    @Override
    public void setDelegate(UnitDelegate delegate) {
        this.delegate = delegate;
    }

    public void setUnitManagerThread(UnitManagerThread unitManagerThread) {
        this.unitManagerThread = unitManagerThread;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getState() {
        return state;
    }

    public void setState(double newState){
        double diff = newState - state;
        this.state = newState;
        for( Unit connection : getConnections(false) ){
            UnitMessage m = new UnitMessageImpl(this, connection, diff, state);
            if(getThread()!=null){
                getThread().publish(connection, m);
            }
        }
    }

    public void addConnections(Collection<Unit> connections, boolean incoming){
        for( Unit connection : connections ){
            addConnection(connection, incoming);
        }
    }

    public void addInboundConnection(Unit newConnection){
        if( inboundingConnections==null ){
            inboundingConnections = new ArrayList<>();
        }
        inboundingConnections.add(newConnection);
    }

    public void addOutboundConnection(Unit newConnection){
        if( outboundingConnections==null ){
            outboundingConnections = new ArrayList<>();
        }
        outboundingConnections.add(newConnection);
    }

    public void addConnection(Unit newConnection, boolean incoming){
        if( ProjectUtility.existsConnection(this, newConnection, incoming) ){
            return;
        }
        if(incoming){
            addInboundConnection(newConnection);
        }
        else{
            addOutboundConnection(newConnection);
        }
    }

    @Override
    public double getUnitState() {
        return state;
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof AloneUnit)){
            return false;
        }
        if( obj == this ){
            return true;
        }
        AloneUnit other = (AloneUnit) obj;
        return other.getName().equals(this.getName());
    }


    @Override
    public String toString() {
        return String.format("[AloneUnit] Name=%s, State=%s", getName(), getState());
    }

    private List<Unit> getInboundingConnections(){
        if(inboundingConnections==null){
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(inboundingConnections);
    }

    private List<Unit> getOutboundingConnections(){
        if(outboundingConnections==null){
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(outboundingConnections);
    }
    @Override
    public List<Unit> getConnections(boolean incoming) {
        if(incoming){
            return getInboundingConnections();
        }
        return getOutboundingConnections();
    }

    @Override
    public void receiveMessage(Unit unit, UnitMessage m) {
        if( delegate!=null ){
            delegate.updateReceived(this, m, this.state);
        }
    }
}
