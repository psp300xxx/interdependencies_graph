package graphs;

import org.junit.Before;
import org.junit.Test;
import utility.ProjectUtility;

import static org.junit.Assert.assertTrue;

public class NDependencyUnitTest {

    private NDependencyUnit unit = new NDependencyUnit("test");

    private final double unitWeightOverConnections = 0.5;

    private final double connectionState = 0.5;
    private final int CONNECTION_NUMBER = 5;

    @Before
    public void addConnections(){
        unit.setUnitWeightOverConnections(unitWeightOverConnections);
        for( int i = 0 ; i<CONNECTION_NUMBER ; i++  ){
            AloneUnit newUnit = new AloneUnit(String.format("aloneUnit%d", i));
            newUnit.setState( connectionState );
            unit.addConnection( newUnit );
        }
    }

    @Test
    public void getCorrectState(){
        double unitState = unit.getState();
        double expectedState = 0.75;
        assertTrue(ProjectUtility.closeEnough(unitState, expectedState));
    }

    @Test
    public void testDifferentWeightComputation(){
        AloneUnit newUnit = new AloneUnit(String.format("aloneUnit%d", CONNECTION_NUMBER+1));
        AloneUnit secondUnit = new AloneUnit(String.format("aloneUnit%d", CONNECTION_NUMBER+2));
        newUnit.setState( connectionState );
        secondUnit.setState(connectionState);
        unit.addConnection(newUnit, 0.3);
        unit.addConnection( secondUnit, 0.2 );
        double sum = 0.0;
        for( Unit connection : unit.getConnections() ){
            sum+=unit.getConnectionWeight(connection);
        }
        assertTrue( ProjectUtility.closeEnough(sum, 1.0) );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConnectionWrongWeight(){
        unit.addConnection(unit, 1.2);
    }

}
