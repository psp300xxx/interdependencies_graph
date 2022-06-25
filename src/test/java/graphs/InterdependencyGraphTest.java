package graphs;

import org.junit.Before;
import org.junit.Test;
import utility.ProjectUtility;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InterdependencyGraphTest {

    private AloneUnit unit = new AloneUnit("startingunit");
    private InterdependencyGraph<Unit> graph = new InterdependencyGraphImpl(unit);
    private final int HIERARCHY_LENGTH = 10;

    private Set<Unit> units;

    @Before
    public void createUnitHierarchy(){
        units = new HashSet<>();
        units.add(unit);
        unit.setState(0.5);
        AloneUnit curr = unit;
        int i = HIERARCHY_LENGTH;
        while(i-->0){
            AloneUnit newUnit = new AloneUnit("Unit"+i);
            newUnit.setState(0.5);
            units.add(newUnit);
            curr.addConnection(newUnit);
            curr = newUnit;
        }
    }
    @Test
    public void meanIsCorrect(){
        double res = graph.getMeanState();
        assertTrue(ProjectUtility.closeEnough(res, 0.5));
    }

    @Test
    public void visitsAllNodes(){
        AtomicInteger visited = new AtomicInteger();
        graph.visitGraph((x)->{
            visited.getAndIncrement();
        });
        assertEquals(HIERARCHY_LENGTH+1, visited.get());
    }

    @Test
    public void nodeListReturnCorrectNodes(){
        List<Unit> computedUnits = graph.getUnitList();
        Set<Unit> setComputedUnits = new HashSet<>(computedUnits);
        assertEquals(setComputedUnits, units);
    }

    @Test
    public void unitStateListCorrect(){
        List<Double> expectedResult = new ArrayList<>();
        for(int i =0 ; i<HIERARCHY_LENGTH+1; i++){
            expectedResult.add(0.5);
        }
        List<Double> actualResult = graph.getUnitStateList();
        assertEquals(actualResult, expectedResult);
    }

}
