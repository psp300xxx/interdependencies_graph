package graphs.publish_subscribe;

import graphs.Unit;

public interface UnitSubscriber {
    void receiveMessage(Unit unit, UnitMessage m );
}
