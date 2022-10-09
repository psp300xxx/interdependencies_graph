package utility;

import graphs.AloneUnit;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ProjectUtilityTest {


    @Test
    public void testExistConnectionReturnsTrue(){
        AloneUnit unit = new AloneUnit("unit");
        AloneUnit secondUnit = new AloneUnit("secondUnit");
        unit.addConnection(secondUnit, true, true);
        assertTrue( ProjectUtility.existsConnection(secondUnit, unit, true) );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBooleanValueParserRaiseExceptionWhenNullIsProvided(){
        String input = null;
        ProjectUtility.getBooleanValue(input);
    }

    @Test
    public void testBooleanValueParserExpectTrue(){
        String input = "True";
        boolean result = ProjectUtility.getBooleanValue(input);
        assertTrue(result);
    }

    @Test
    public void testBooleanValueParserExpectTrueWhenTrueIsProvidedInMixedChars(){
        String input = "TrUe";
        boolean result = ProjectUtility.getBooleanValue(input);
        assertTrue(result);
    }

    @Test
    public void testBooleanValueParserExpectFalse(){
        String input = "false";
        boolean result = ProjectUtility.getBooleanValue(input);
        assertFalse(result);
    }

    @Test
    public void testExistMiddleConnectionReturnsFalse(){
        AloneUnit unit = new AloneUnit("unit");
        AloneUnit secondUnit = new AloneUnit("secondUnit");
        unit.addOutboundConnection(secondUnit);
        assertFalse( ProjectUtility.existsConnection(unit, secondUnit, true) );
    }

    @Test
    public void testConnectionReturnsFalseWhenThereIsNoConnection(){
        AloneUnit unit = new AloneUnit("unit");
        AloneUnit secondUnit = new AloneUnit("secondUnit");
        assertFalse( ProjectUtility.existsConnection(unit, secondUnit, true) );
    }

}
