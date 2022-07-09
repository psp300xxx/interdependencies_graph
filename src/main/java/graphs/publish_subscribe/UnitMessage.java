package graphs.publish_subscribe;

import graphs.Unit;

public interface UnitMessage {

    Unit getSenderUnit();

    Unit getReceiverUnit();

    double getStateVariation();

    double getCurrentState();

}
