package graphs.publish_subscribe;

import graphs.Unit;

public class UnitMessageImpl implements UnitMessage{

    private Unit sender;

    private Unit receiver;

    private double diff;

    private double currentState;


    public UnitMessageImpl(Unit sender, Unit receiver, double diff, double currentState){
        this.sender=sender;
        this.receiver = receiver;
        this.diff = diff;
        this.currentState = currentState;
    }

    @Override
    public Unit getSenderUnit() {
        return sender;
    }

    @Override
    public Unit getReceiverUnit() {
        return receiver;
    }

    @Override
    public double getStateVariation() {
        return diff;
    }

    @Override
    public double getCurrentState() {
        return currentState;
    }
}
