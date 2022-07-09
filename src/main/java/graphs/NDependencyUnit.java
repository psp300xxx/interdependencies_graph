package graphs;

import graphs.publish_subscribe.UnitDelegate;
import graphs.publish_subscribe.UnitManagerThread;
import graphs.publish_subscribe.UnitMessage;
import graphs.publish_subscribe.UnitMessageImpl;
import org.json.JSONArray;
import org.json.JSONObject;
import utility.ProjectUtility;

import java.util.*;

public class NDependencyUnit implements Unit{

    private UnitDelegate delegate;

    private double unitState = 1.0;

    private UnitManagerThread unitManagerThread;

    private WeightingFunction weightingFunction = new SoftMaxWeightingFunction();

    private String name;

    private Map<Unit, Double> inboundConnectionsRateValues = new HashMap<>() ;

    private List<Unit> outboundConnections;

    @Override
    public double getUnitState() {
        return unitState;
    }


    public NDependencyUnit(String name){
        this.name = name;
    }

    public void setWeightingFunction(WeightingFunction function){
        this.weightingFunction = function;
    }

    @Override
    public UnitManagerThread getThread() {
        return unitManagerThread;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setUnitManagerThread(UnitManagerThread unitManagerThread) {
        this.unitManagerThread = unitManagerThread;
    }

    public double getConnectionWeight(Unit connection){
        int n = inboundConnectionsRateValues.keySet().size();
        if (inboundConnectionsRateValues.size() == 1){
            return 1.0;
        }
        double weight = inboundConnectionsRateValues.get(connection);
        return weight;
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
        return String.format("[NDependencyUnit] Name=%s, State=%s", getName(), getUnitState());
    }

    @Override
    public double getState() {
        return unitState;
    }

    public void setUnitState(double unitState){
        double diff = unitState - this.unitState;
        this.unitState = unitState;
        for( Unit connection : getConnections(false) ){
            UnitMessage m = new UnitMessageImpl(this, connection, diff, this.unitState);
            if(getThread()!=null){
                getThread().publish(connection, m);
            }
        }
    }

    @Override
    public UnitDelegate getDelegate() {
        return delegate;
    }

    @Override
    public void setDelegate(UnitDelegate delegate) {
        this.delegate = delegate;
    }


    @Override
    public List<Unit> getInboundConnections(){
        if( inboundConnectionsRateValues==null || inboundConnectionsRateValues.keySet()==null ){
            return Collections.emptyList();
        }
        return new ArrayList<>(inboundConnectionsRateValues.keySet());
    }

    @Override
    public List<Unit> getConnections(boolean incoming) {
        if(incoming){
            return getInboundConnections();
        }
        return getOutboundConnections();
    }

    @Override
    public List<Unit> getOutboundConnections(){
        if( outboundConnections==null ){
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(outboundConnections);
    }

    private void adaptWeightAddingNewOne(Unit newUnit, double newWeight){
        if(inboundConnectionsRateValues == null) {
            inboundConnectionsRateValues = new HashMap<>();
            inboundConnectionsRateValues.put(newUnit, newWeight);
            return;
        }
        double remainderWeight = 1-newWeight;
        Collection<Double> weights = ProjectUtility.copyCollection(inboundConnectionsRateValues.values());
        List<Double> newWeights = this.weightingFunction.getNewWeights(weights);
        int curr = 0;
        for( Unit unit : inboundConnectionsRateValues.keySet() ){
            inboundConnectionsRateValues.put(unit, newWeights.get(curr++)*remainderWeight);
        }
        inboundConnectionsRateValues.put(newUnit, newWeight);
    }

    public void addConnection(Unit unit, boolean incoming){
        addConnection(unit, incoming, null);
    }

    public void addConnection(Unit unit, boolean incoming, Double weight){
        if( ProjectUtility.existsConnection(this, unit, incoming) ){
            return;
        }
        if(incoming){
            addInboundConnection(unit, weight);
        }
        else{
            addOutboundConnection(unit);
        }
    }


    public void addOutboundConnection(Unit unit){
        if( outboundConnections==null ){
            outboundConnections = new ArrayList<>();
        }
        outboundConnections.add(unit);
    }

    @Override
    public void receiveMessage(Unit unit, UnitMessage m) {
        if( delegate!=null ){
            delegate.updateReceived(this, m, this.unitState);
        }
        System.out.println(inboundConnectionsRateValues);
        double connectionWeight = inboundConnectionsRateValues.get(unit);
        double newState = this.unitState + ( connectionWeight*m.getStateVariation() );
        if( !ProjectUtility.closeEnough(this.unitState, newState) ){
            this.setUnitState(newState);
        }
        if( delegate!=null ){
            delegate.updateApplied(this, m, this.unitState);
        }
    }

    public static class WeightSumNotCloseTo1Exception extends RuntimeException {
        private List<Double> weights;

        public WeightSumNotCloseTo1Exception(List<Double> weights){
            this.weights = weights;
        }

        @Override
        public String getMessage() {
            return String.format("Weights: %s have no sum close to 1", weights);
        }
    }

    public void setManualWeights(List<Double> weights){
        if( weights.size()!=inboundConnectionsRateValues.size() ){
            throw new IllegalArgumentException("Each connection has to have its own weight");
        }
        double weightSum = weights.stream().reduce( (x,y)->{return x+y;} ).get();
        if( !ProjectUtility.closeEnough(weightSum, 1.0) ){
            throw new WeightSumNotCloseTo1Exception(weights);
        }
        int curr = 0;
        for( Unit connection : inboundConnectionsRateValues.keySet() ){
            inboundConnectionsRateValues.put(connection, weights.get(curr++));
        }
    }


    public void addInboundConnection(Unit unit, Double weight){
        if( weight!=null && (weight>1.0 || weight<0) ){
            throw new IllegalArgumentException( String.format("Weight has to be in [0,1] range, %d is not correct", weight) );
        }
        double connectionWeight = weight != null ? weight : 0.01;
        adaptWeightAddingNewOne(unit, connectionWeight);
    }
}
