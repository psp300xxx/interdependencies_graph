package graphs;

import jdk.jshell.spi.ExecutionControl;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GraphImpl implements Graph<Unit>{

    private static Logger LOGGER =  Logger.getLogger("GraphImpl");


    public GraphImpl(Unit startingUnit, String name,int threadNumber){
        this.startingUnit = startingUnit;
        this.numberOfThreads = threadNumber;
        this.name = name;
    }

    public GraphImpl(Unit startingUnit, String name){
        this(startingUnit, name, Runtime.getRuntime().availableProcessors());
    }

    public GraphImpl(String name){
        this(null, name, Runtime.getRuntime().availableProcessors());
    }

    public final static Predicate<Double> DEFAULT_PREDICATE = (aDouble -> aDouble==null || (aDouble!=null && aDouble>=0.0 && aDouble<=1.0));

    private String name;

    private boolean isUnitListValid = false;

    private Set<Unit> units;

    private boolean isGraphRunning = false;

    private Predicate<Double> acceptablePredicate = DEFAULT_PREDICATE ;

    private Map<Unit, Set<Connection>> connectionMap = new HashMap<>();

    private Map<Unit, UnitManagerThread> unitThreadMap;

    private Unit startingUnit;

    private int numberOfThreads;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Predicate<Double> getNewStateIsAcceptablePredicate() {
        return acceptablePredicate;
    }

    @Override
    public Map<Unit, Set<Connection>> getConnectionMap() {
        return Collections.unmodifiableMap(connectionMap);
    }

    @Override
    public void startUpdates() throws GraphIsRunningException{
        if(isGraphRunning){
            throw new GraphIsRunningException(this);
        }
        if(startingUnit==null){
            throw new RuntimeException();
        }
        Set<Unit> unitList = getUnits();
        List<UnitManagerThread> threads = new ArrayList<>();
        LOGGER.info("Starting graph: "+ getName());
        for( int i = 0; i<this.numberOfThreads(); i++ ){
            threads.add( new UnitManagerThread()  );
        }
        assignThreadToUnit(unitList, threads);
        LOGGER.info("Threads: "+this.unitThreadMap);
        for( UnitManagerThread thread : threads ){
            thread.start();
        }
        isGraphRunning = true;
    }

    public void stopUpdates(){
//        TODO: Implement
        for(UnitManagerThread thread : new HashSet<>(unitThreadMap.values())){
            thread.stopThread();
        }
        isGraphRunning = false;
    }

    private void assignThreadToUnit(Set<Unit> units, List<UnitManagerThread> threads){
        int index = 0;
        if(unitThreadMap==null){
            unitThreadMap = new HashMap<>();
        }
        unitThreadMap.clear();
        for(Unit unit : units){
            int currentIndex = index++ % threads.size();
            unitThreadMap.put(unit, threads.get(currentIndex));
        }
    }

    @Override
    public Unit getStartingUnit() {
        return startingUnit;
    }

    @Override
    public void deliverMessage(Unit sender, UnitMessage unitMessage) {
        Set<Connection> connections = connectionMap.get(sender);
        if(connections!=null){
            for( Connection connection : connections ){
                UnitMessage message = unitMessage.copy();
                if(connection.weight() !=null){
                    message.setConnectionWeight(connection.weight());
                }
                message.setDestination(connection.getTo());
                UnitManagerThread thread = unitThreadMap.get(connection.getTo());
                thread.addMessageInQueue(message);
            }
        }
    }

    @Override
    public Set<Unit> getUnits() {
        if(isUnitListValid){
            return this.units;
        }
        units = new HashSet<>();
        visitGraph((unit) -> units.add(unit));
        return units;
    }

    private Unit getNamedNode(String name){
        Set<Unit> units = getUnits();
        List<Unit> unit = units.stream().filter((x)->x.getName().equals(name)).collect(Collectors.toList());
        if(unit.isEmpty()){
            return null;
        }
        return unit.get(0);
    }

    @Override
    public void addConnection(String from, String to, Double weight) throws GraphIsRunningException {
        List<String> missing = new LinkedList<>();
        Unit source = getNamedNode(from);
        boolean isSourceMissing = source==null;
        if(isSourceMissing){
            missing.add(from);
        }
        Unit dest = getNamedNode(to);
        boolean isDestMissing = dest==null;
        if(isDestMissing){
            missing.add(to);
        }
        if ( !missing.isEmpty() ){
            throw new UnitNotInGraphException(missing);
        }
        addConnection(source, dest, weight);
    }

    @Override
    public void addConnection(Unit from, Unit to, Double weight) throws GraphIsRunningException {
        Set<Connection> connections = connectionMap.get(from);
        if(isGraphRunning){
            throw new GraphIsRunningException(this);
        }
        if(connections==null){
            connections = new HashSet<>();
        }
        Connection newConnection = new ConnectionImpl(from, to, weight);
        connections.add(newConnection);
        connectionMap.put(from, connections);
        isUnitListValid = false;
    }

    @Override
    public void addConnection(String from, String to) throws GraphIsRunningException {
        addConnection(from, to, null);
    }

    @Override
    public void addConnection(Unit from, Unit to) throws GraphIsRunningException {
        addConnection(from, to, null);
    }

    @Override
    public int numberOfThreads() {
        return numberOfThreads;
    }

    @Override
    public void setStartingNode(Unit newStartingNode) {
        if(isGraphRunning){
            return;
        }
        this.startingUnit = newStartingNode;
    }
}
