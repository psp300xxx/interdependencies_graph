package graphs.publish_subscribe;

import graphs.Unit;

public interface UnitPublisher {

    void publish(Unit unit, UnitMessage m);

}
