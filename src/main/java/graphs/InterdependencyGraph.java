package graphs;

import org.jgrapht.Graph;
import org.jgrapht.graph.AsUnweightedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;
import java.util.function.Consumer;

public interface InterdependencyGraph<T extends Unit>  {

    List<T> getUnitList();

    T getStartingUnit();

    default List<Double> getUnitStateList(){
        List<Double> result = new ArrayList<>();
        for( T unit : getUnitList() ){
            result.add(unit.getState());
        }
        return result;
    }

    void visitGraph(Consumer<Unit> consumer);

    default double getMeanState(){
        Double mean = null;
        int visited = 0;
        Set<Unit> visitedUnits = new HashSet<>();
        Queue<Unit> unitQueue = new LinkedList<>();
        unitQueue.add(getStartingUnit());
        while(!unitQueue.isEmpty()){
            Unit nextUnit = unitQueue.poll();
            visitedUnits.add(nextUnit);
            for( Unit edgeUnit : nextUnit.getConnections() ){
                if(!visitedUnits.contains(edgeUnit)){
                    unitQueue.add(edgeUnit);
                }
            }
            if(mean==null){
                mean = nextUnit.getState();
                visited++;
                continue;
            }
            visited++;
            mean = ( mean * ((double)(visited-1))/((double)visited) ) + (nextUnit.getState() * (1/(double)visited) );
        }
        return mean;
    }


}
