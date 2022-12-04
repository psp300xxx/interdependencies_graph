package graphs;

import org.junit.Test;

public class TestUnit {

    @Test(expected = RuntimeException.class)
    public void unit_not_accept_null_graph(){
        new UnitImpl(null, "", 0.2);
    }

}
