package graphs;

import java.util.*;
import java.util.function.Consumer;

public class InterdependencyGraphImpl implements InterdependencyGraph<Unit>{

    private Unit startingUnit;

    public InterdependencyGraphImpl(Unit startingUnit){
        this.startingUnit = startingUnit;
    }

    public void visitGraph(Consumer<Unit> consumer){
        Queue<Unit> queue = new LinkedList<>();
        queue.add( startingUnit  );
        Set<Unit> visitedSet = new HashSet<>();
        while( ! queue.isEmpty() ){
            Unit nextUnit = queue.poll();
            consumer.accept(nextUnit);
            visitedSet.add(nextUnit);
            for( Unit edgeUnit : nextUnit.getConnections() ){
                if( !visitedSet.contains(edgeUnit) ){
                    queue.add(edgeUnit);
                }
            }
        }
    }


    @Override
    public List<Unit> getUnitList() {
        Queue<Unit> queue = new LinkedList<>();
        queue.add( startingUnit  );
        List<Unit> units = new LinkedList<>();
        Set<Unit> visitedSet = new HashSet<>();
        while( ! queue.isEmpty() ){
            Unit nextUnit = queue.poll();
            units.add(nextUnit);
            visitedSet.add(nextUnit);
            for( Unit edgeUnit : nextUnit.getConnections() ){
                if( !visitedSet.contains(edgeUnit) ){
                    queue.add(edgeUnit);
                }
            }
        }
        return units;
    }

    @Override
    public Unit getStartingUnit() {
        return startingUnit;
    }
}
