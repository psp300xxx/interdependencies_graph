package rest_controllers;

import graphs.InterdependencyGraph;
import graphs.Unit;

public interface GraphManager {

    void createNewGraph(String name) throws GraphExistingException;

    InterdependencyGraph<Unit> getNamedGraph(String name);


}
