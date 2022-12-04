package graphs;

import org.junit.Assert;
import org.junit.Test;

public class TestGraphUtility {

    @Test
    public void test_closeEnough(){
        double a = 1.0;
        double b = 0.999999999;
        Assert.assertTrue( GraphUtility.isCloseEnough(a,b) );
    }

    @Test
    public void test_closeEnoughFalse(){
        double a = 1.0;
        double b = 0.99999;
        Assert.assertFalse( GraphUtility.isCloseEnough(a,b) );
    }

}
