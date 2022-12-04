package graphs;

public class UpdateStateMessage implements UnitMessage{

    private Unit sourceUnit;

    private Unit destination;

    private Double oldState, newState, connectionWeight;

    public UpdateStateMessage(Unit unit, Double oldState, Double newState, Double connectionWeight){
        this.sourceUnit = unit;
        this.newState = newState;
        this.oldState = oldState;
        this.connectionWeight = connectionWeight;
    }

    public UpdateStateMessage(Unit unit, Double oldState, Double newState){
        this(unit, oldState,newState, null);
    }

    public double getUpdateDifference(){
        return newState - oldState;
    }


    public Double getNewState() {
        return newState;
    }

    public void setConnectionWeight(Double connectionWeight) {
        this.connectionWeight = connectionWeight;
    }

    public Double getConnectionWeight() {
        if(connectionWeight==null){
            return 0.1;
        }
        return connectionWeight;
    }

    @Override
    public Unit getSource() {
        return sourceUnit;
    }

    public Double getOldState() {
        return oldState;
    }

    public void setDestination(Unit destination){
        this.destination = destination;
    }

    @Override
    public Unit getDestination() {
        return destination;
    }

    @Override
    public UnitMessage copy() {
        UpdateStateMessage message = new UpdateStateMessage(sourceUnit,oldState, newState, connectionWeight);
        message.destination = destination;
        return message;
    }
}
