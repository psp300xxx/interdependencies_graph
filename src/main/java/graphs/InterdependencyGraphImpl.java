package graphs;

import graphs.publish_subscribe.UnitManagerThread;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class InterdependencyGraphImpl implements InterdependencyGraph<Unit>{

    private Unit startingUnit;
    private Map<String, Unit> unitMap;

    private String name;
    private AtomicBoolean updateStarted = new AtomicBoolean(false);

    private int numberOfThreads;

    private List<UnitManagerThread> threads;

    public InterdependencyGraphImpl(Unit startingUnit, String name, int numberOfThreads){
        this.startingUnit = startingUnit;
        this.name = name;
        this.numberOfThreads = numberOfThreads;
    }

    public String getName() {
        return name;
    }

    public void startUpdates(){
        assignUnitToThreads();
        startThreads();
        updateStarted.compareAndSet(false, true);
    }

    private void startThreads(){
        for( UnitManagerThread thread : threads ){
            thread.start();
        }
    }

    private int threadIndex( int unitPerThreads, int numberOfThreads, int count ){
        int threadId = count / unitPerThreads;
        if( threadId>=numberOfThreads ){
            threadId = numberOfThreads-1;
        }
        return threadId;
    }


    private void assignUnitToThreads(){
        List<Unit> unitList = getUnitList();
        int numberOfUnitsPerThread = 1;
        int remainder = 0;
        if( this.numberOfThreads > unitList.size()){
            numberOfUnitsPerThread = unitList.size() / this.numberOfThreads;
        }
        if(numberOfUnitsPerThread==0){
            numberOfUnitsPerThread=1;
        }
        threads = new ArrayList<>();
        for( int i = 0; i<this.numberOfThreads; i++ ){
            threads.add( new UnitManagerThread() );
        }
        int count = 0;
        Iterator<Unit> it = unitList.iterator();
        while( it.hasNext() ){
            int threadId = threadIndex( numberOfUnitsPerThread, this.numberOfThreads, count );
            Unit nextUnit = it.next();
            nextUnit.setUnitManagerThread(threads.get(threadId));
            count++;
        }
    }

    public void visitGraph(Consumer<Unit> consumer){
        Queue<Unit> queue = new LinkedList<>();
        queue.add( startingUnit  );
        Set<Unit> visitedSet = new HashSet<>();
        while( ! queue.isEmpty() ){
            Unit nextUnit = queue.poll();
            consumer.accept(nextUnit);
            visitedSet.add(nextUnit);
            for( Unit edgeUnit : nextUnit.getConnections(false) ){
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
            for( Unit edgeUnit : nextUnit.getConnections(false) ){
                if( !visitedSet.contains(edgeUnit) ){
                    queue.add(edgeUnit);
                }
            }
        }
        return units;
    }

    @Override
    public Unit getNamedNode(String nodeName) {
        return unitMap.get(nodeName);
    }

    @Override
    public Unit getStartingUnit() {
        return startingUnit;
    }
}
