package graphs;

import java.util.List;

public interface Unit {

    String getName();
    double getState();

    List<Unit> getConnections();


}
