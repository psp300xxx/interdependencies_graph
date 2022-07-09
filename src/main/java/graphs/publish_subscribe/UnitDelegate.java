package graphs.publish_subscribe;

import graphs.Unit;

public interface UnitDelegate {

    void updateReceived(Unit unit, UnitMessage messageReceived, double currentState);
    void updateApplied(Unit unit, UnitMessage message, double newState);

}
