package graphs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateStateMessage implements UnitMessage{

    private Unit sourceUnit;

    private static Logger LOGGER =  LoggerFactory.getLogger("UpdateStateMessage");

    public static final double DEFAULT_WEIGHT = 0.1;

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

    @Override
    public String toString() {
        return String.format("[Message] From='%s', To='%s', oldState='%.2f',newState='%.2f'", sourceUnit, destination, oldState, newState);
    }

    public Double getNewState() {
        return newState;
    }

    public void setConnectionWeight(Double connectionWeight) {
        this.connectionWeight = connectionWeight;
    }

    public Double getConnectionWeight() {
        if(connectionWeight==null){
            LOGGER.warn("Weight is null, returning default value: "+DEFAULT_WEIGHT);
            return DEFAULT_WEIGHT;
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
