package graphs;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface Graph<T extends Unit> {

    String getName();

    Predicate<Double> getNewStateIsAcceptablePredicate();

    Map<T, Set<Connection>> getConnectionMap();

    void setUnitDelegate(UnitDelegate delegate, Predicate<Unit> applyToUnit);

    default void setUnitDelegate(UnitDelegate delegate){
        setUnitDelegate(delegate, (x)->true);
    }

    void startUpdates() throws GraphIsRunningException;

    void stopUpdates();

    T getStartingUnit();

    void deliverMessage(Unit sender, UnitMessage unitMessage);

    Set<T> getUnits();

    void addConnection(String from, String to, Double weight) throws GraphIsRunningException;

    void addConnection(T from, T to, Double weight) throws GraphIsRunningException;

    void addConnection(String from, String to) throws GraphIsRunningException;

    void addConnection(T from, T to) throws GraphIsRunningException;

    int numberOfThreads();

    void setStartingNode(T newStartingNode);

    default void  visitGraph(Consumer<Unit> consumer){
        Queue<Unit> queue = new LinkedList<>();
        queue.add(getStartingUnit());
        Set<Unit> visitedUnits = new HashSet<>();
        while(!queue.isEmpty()){
            Unit nextUnit = queue.poll();
            visitedUnits.add(nextUnit);
            consumer.accept(nextUnit);
            Set<Connection> connections = getConnectionMap().get(nextUnit);
            if(connections==null){
                continue;
            }
            for( Connection connection : connections ){
                if( !visitedUnits.contains(connection.getTo()) ){
                    queue.add(connection.getTo());
                }
            }
        }
    }


}
