package rest_controllers;

import graphs.InterdependencyGraph;
import graphs.Unit;

public interface GraphManagerDelegate {

    void graphCreated(GraphManager manager, InterdependencyGraph<Unit> newGraph);

    void graphCreationFailed(GraphManager manager, String name, Exception exception);

}
