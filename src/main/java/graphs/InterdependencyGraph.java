package graphs;

import org.jgrapht.Graph;
import org.jgrapht.graph.AsUnweightedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;

public interface InterdependencyGraph<T extends Unit>  {

    List<T> getUnitList();

    default double getMeanState(){
        Double mean = null;
        int visited = 0;
        Set<Unit> visitedUnits = new HashSet<>();
        Queue<Unit> unitQueue = new LinkedList<>();
        unitQueue.add(getUnitList().get(0));
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
            mean = ( mean * (visited-1/visited) ) + (nextUnit.getState() * (1/visited) );
        }
        return mean;
    }


}
