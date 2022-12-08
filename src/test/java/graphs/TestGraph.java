package graphs;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestGraph {

    private Graph<Unit> graph;

    private Map<String, AtomicBoolean> received;

    private DelegateImpl delegate = new DelegateImpl();

    private final int NUMBER_OF_NODES = 5;

    public void setup() throws GraphIsRunningException {
        setup(null);
    }

    public void setup(Double weight) throws GraphIsRunningException {
        org.apache.log4j.BasicConfigurator.configure();
        received = new HashMap<>();
        Unit startingUnit = createUnit("start", 1.0);
        graph = new GraphImpl("graph");
        startingUnit.setGraph(graph);
        startingUnit.setDelegate(delegate);
        graph.setStartingNode(startingUnit);
        received.put(startingUnit.getName(), new AtomicBoolean(false));
        Unit currentFrom = startingUnit;
        for( int i = 0; i<NUMBER_OF_NODES; i++ ){
            Unit newUnit = createUnitOfGraph(graph, String.format("subunit_%d", i),1.0, currentFrom.getName());
            newUnit.setDelegate(delegate);
            received.put(newUnit.getName(), new AtomicBoolean(false));
            graph.addConnection(currentFrom, newUnit, weight);
            currentFrom = newUnit;
        }
        graph.addConnection(currentFrom.getName(), startingUnit.getName(), weight);
    }

    public class DelegateImpl implements UnitDelegate {
        @Override
        public void updatedReceived(Unit unit, UnitMessage message) {
            AtomicBoolean atomicBoolean = TestGraph.this.received.get(unit.getName());
            atomicBoolean.compareAndSet(false, true);
        }
    }

    private Unit createUnitOfGraph(Graph graph, String name, Double state, String from){
        Unit unit = new UnitImpl(graph, name, state);
        return unit;
    }

    private Unit createUnit(String name, Double weight){
        Unit unit = new UnitImpl(name, weight);
        return unit;
    }

    private boolean allTrue(){
        for( Map.Entry<String, AtomicBoolean> entry : received.entrySet() ){
            if(!entry.getValue().get()){
                return false;
            }
        }
        return true;
    }

    @Test
    public void startDeliverTest() throws GraphIsRunningException, InterruptedException {
        setup();
        graph.startUpdates();
        graph.getStartingUnit().setState(0.8);
        final int TIMEOUT_SECONDS = 30;
        int i = 0;
        while(i++<TIMEOUT_SECONDS){
            if(allTrue()){
                return;
            }
            Thread.sleep(1000);
        }
        Assert.fail("There is at least a never touched node: "+ received);
    }

    @Test
    public void startDeliverTestWithWeights() throws GraphIsRunningException, InterruptedException {
        setup(0.5);
        graph.startUpdates();
        graph.getStartingUnit().setState(0.8);
        final int TIMEOUT_SECONDS = 30;
        int i = 0;
        while(i++<TIMEOUT_SECONDS){
            if(allTrue()){
                return;
            }
            Thread.sleep(1000);
        }
        Assert.fail("There is at least a never touched node: "+ received);
    }

    @Test
    public void test_default_predicate_when_null(){
        Double input = null;
        boolean result = GraphImpl.DEFAULT_PREDICATE.test(input);
        Assert.assertTrue(result);
    }

    @Test
    public void test_default_predicate_when_outOfBoundsIsProvided(){
        Double input = 2.0;
        boolean result = GraphImpl.DEFAULT_PREDICATE.test(input);
        Assert.assertFalse(result);
    }

    @Test(expected = GraphIsRunningException.class)
    public void cannotOperateOnGraphWhileRunning() throws GraphIsRunningException {
        setup();
        graph.startUpdates();
        Unit unit = new UnitImpl(graph, "unit",0.2);
        graph.addConnection(graph.getStartingUnit(), unit);
    }

    @Test
    public void startAndStopTest() throws GraphIsRunningException, InterruptedException {
        setup();
        graph.startUpdates();
        Unit unit = new UnitImpl(graph, "unit",0.2);
        graph.stopUpdates();
        AtomicBoolean newNodeTouched = new AtomicBoolean(false);
        unit.setDelegate(new UnitDelegate() {
            @Override
            public void updatedReceived(Unit unit, UnitMessage message) {
                newNodeTouched.compareAndSet(false, true);
            }
        });
        graph.addConnection(graph.getStartingUnit(), unit);
        graph.startUpdates();
        int secondsTimeout = 30;
        // To trigger message exchange
        graph.getStartingUnit().setState(0.8);
        while(!newNodeTouched.get() && secondsTimeout-- > 0){
            Thread.sleep(1000);
        }
        Assert.assertTrue(newNodeTouched.get());
    }

    @Test(expected = UnitNotInGraphException.class)
    public void addConnectionStringMissingSecond() throws GraphIsRunningException {
        setup();
        graph.addConnection("subunit_0", "subunit_9");
    }

    @Test(expected = UnitNotInGraphException.class)
    public void addConnectionStringMissingFirst() throws GraphIsRunningException {
        setup();
        graph.addConnection("not_subunit_0", "subunit_0");
    }

    @Test(expected = GraphIsRunningException.class)
    public void cannotStartGraphTwice() throws GraphIsRunningException {
        setup();
        graph.startUpdates();
        graph.startUpdates();
    }

    @Test
    public void test_default_predicate_when_inBoundsIsProvided(){
        Double input = 0.5;
        boolean result = GraphImpl.DEFAULT_PREDICATE.test(input);
        Assert.assertTrue(result);
    }

    @Test(expected = RuntimeException.class)
    public void cannotStartGraphWithNoStartingUnit() throws GraphIsRunningException {
        setup();
        graph.setStartingNode(null);
        graph.startUpdates();
    }

}
