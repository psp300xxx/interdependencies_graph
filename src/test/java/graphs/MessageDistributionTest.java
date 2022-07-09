package graphs;

import graphs.publish_subscribe.UnitDelegate;
import graphs.publish_subscribe.UnitMessage;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class MessageDistributionTest {

    private int numberOfThreads = Runtime.getRuntime().availableProcessors();
    private int numberOfNodes = 5;
    private InterdependencyGraph<Unit> interdependencyGraph;

    private int seconds = 30;

    private long TIMEOUT = seconds*1000; //30 Seconds

    private AloneUnit startingUnit;

    private Map<Unit, Boolean> updateReceivedMap;

    public class TestDelegate implements UnitDelegate{

        @Override
        public void updateReceived(Unit unit, UnitMessage messageReceived, double currentState) {
            System.out.println("Updating value "+messageReceived.getReceiverUnit().getName());
            MessageDistributionTest.this.updateReceivedMap.put(messageReceived.getReceiverUnit(), Boolean.TRUE);
        }

        @Override
        public void updateApplied(Unit unit, UnitMessage message, double newState) {

        }
    }

    @Before
    public void setup(){
        TestDelegate delegate = new TestDelegate();
        startingUnit = new AloneUnit("starting unit");
        startingUnit.setState(1.0);
        Unit currentNode = startingUnit;
        updateReceivedMap = new HashMap<>();
        updateReceivedMap.put(startingUnit, Boolean.TRUE);
        startingUnit.setDelegate(delegate);
        NDependencyUnit newNode;
        for( int i = 1; i<numberOfNodes; i++ ){
            newNode = new NDependencyUnit("node "+i);
            updateReceivedMap.put(newNode, Boolean.FALSE);
            newNode.setDelegate(delegate);
            newNode.addConnection(currentNode, true, true);
            currentNode = newNode;
        }
        interdependencyGraph = new InterdependencyGraphImpl(startingUnit, "test", numberOfThreads);
        interdependencyGraph.startUpdates();
    }

    private boolean everybodyCompleted(){
        boolean res = updateReceivedMap.values().stream().allMatch((x)->x.equals(Boolean.TRUE));
        return res;
    }

    @Test
    public void everyNodeReceivesANotification() throws InterruptedException {
        startingUnit.setState(0.3);
        int elapsedSeconds = 0;
        while( !everybodyCompleted() && elapsedSeconds++<seconds ){
            Thread.sleep(1000);
        }
        assertTrue( everybodyCompleted() );
    }

}
