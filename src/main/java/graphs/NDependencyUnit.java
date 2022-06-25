package graphs;

import utility.ProjectUtility;

import java.util.*;

public class NDependencyUnit implements Unit{

    private double unitState = 1.0;

    private WeightingFunction weightingFunction = new SoftMaxWeightingFunction();

    private String name;

    private Map<Unit, Double> dependencyRateValues;

    private double unitWeightOverConnections = 1.0;


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

    @Override
    public double getState() {
        double mean =  (unitState * unitWeightOverConnections );
        double connectionMean = 0.0;
        for( Unit connection : getConnections() ){
            double currentWeight = getConnectionWeight(connection);
            connectionMean += connection.getState() * currentWeight;
        }
        return mean + ( 1 - unitWeightOverConnections ) * connectionMean;
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
