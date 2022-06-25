package graphs;

import utility.ProjectUtility;

import java.util.*;

public class NDependencyUnit implements Unit{

    private double unitState = 1.0;

    private WeightingFunction weightingFunction = new SoftMaxWeightingFunction();

    private String name;

    private Map<Unit, Double> dependencyRateValues;

    private double unitWeightOverConnections = 1.0;

    @Override
    public double getUnitState() {
        return unitState;
    }

    private List<Unit> connections;

    public NDependencyUnit(String name){
        this.name = name;
    }

    public void setWeightingFunction(WeightingFunction function){
        this.weightingFunction = function;
    }

    @Override
    public String getName() {
        return name;
    }

    public double getUnitWeightOverConnections(){
        return unitWeightOverConnections;
    }

    public void setUnitWeightOverConnections(double newWeightOverConnections){
        if( newWeightOverConnections>1.0 || newWeightOverConnections<0.0 ){
            throw new IllegalArgumentException("Weight must be in [0,1] range");
        }
        this.unitWeightOverConnections = newWeightOverConnections;
    }

    public double unitStateWeightOverConnections(){
        return unitWeightOverConnections;
    }

    public boolean anyConnectionHasSameWeight(){
        return dependencyRateValues==null;
    }

    public double getConnectionWeight(Unit connection){
        int n = connections.size();
        if (dependencyRateValues.size() == 1){
            return 1.0;
        }
        double weight = dependencyRateValues.get(connection);
        return weight;
    }

    public double getState(Set<Unit> visitedUnits) {
        if(visitedUnits.contains(this)){
            return getUnitState();
        }
        visitedUnits.add(this);
        double mean =  (unitState * unitWeightOverConnections );
        double connectionMean = 0.0;
        for( Unit connection : getConnections() ){
            double currentWeight = getConnectionWeight(connection);
            connectionMean += connection.getState(visitedUnits) * currentWeight;
        }
        return mean + ( 1 - unitWeightOverConnections ) * connectionMean;
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
        return getState( new HashSet<>() );
    }

    public void setUnitState(double unitState){
        this.unitState = unitState;
    }

    @Override
    public List<Unit> getConnections() {
        return connections;
    }

    private void adaptWeightAddingNewOne(Unit newUnit, double newWeight){
        if(dependencyRateValues == null) {
            dependencyRateValues = new HashMap<>();
            dependencyRateValues.put(newUnit, newWeight);
            return;
        }
        dependencyRateValues.put(newUnit, newWeight);
        Collection<Double> weights = ProjectUtility.copyCollection(dependencyRateValues.values());
        List<Double> newWeights = this.weightingFunction.getNewWeights(weights);
        int curr = 0;
        for( Unit unit : dependencyRateValues.keySet() ){
            dependencyRateValues.put(unit, newWeights.get(curr++));
        }
    }

    public void addConnection(Unit unit){
        addConnection(unit, null);
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
        if( weights.size()!=dependencyRateValues.size() ){
            throw new IllegalArgumentException("Each connection has to have its own weight");
        }
        double weightSum = weights.stream().reduce( (x,y)->{return x+y;} ).get();
        if( !ProjectUtility.closeEnough(weightSum, 1.0) ){
            throw new WeightSumNotCloseTo1Exception(weights);
        }
        int curr = 0;
        for( Unit connection : dependencyRateValues.keySet() ){
            dependencyRateValues.put(connection, weights.get(curr++));
        }
    }


    public void addConnection(Unit unit, Double weight){
        if( weight!=null && (weight>1.0 || weight<0) ){
            throw new IllegalArgumentException( String.format("Weight has to be in [0,1] range, %d is not correct", weight) );
        }
        if(connections==null){
            connections = new ArrayList<>();
        }
        connections.add(unit);
        double connectionWeight = weight != null ? weight : 0.01;
        adaptWeightAddingNewOne(unit, connectionWeight);
    }
}
