package rest_controllers;

import graphs.AloneUnit;
import graphs.InterdependencyGraph;
import graphs.InterdependencyGraphImpl;
import graphs.Unit;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GraphManagerImpl implements GraphManager{

    private Map<String, InterdependencyGraph<Unit>> graphs;

    private Optional<GraphManagerDelegate> delegate = Optional.empty() ;
    @Override
    public void createNewGraph(String name) throws GraphExistingException {
        if(graphs==null){
            graphs = new HashMap<>();
        }
        if(graphs.containsKey(name)){
            throw new GraphExistingException(name);
        }
        InterdependencyGraph<Unit> newGraph = new InterdependencyGraphImpl(new AloneUnit("unit0"), name, Runtime.getRuntime().availableProcessors());
        delegate.ifPresent((x)-> {x.graphCreated(GraphManagerImpl.this, newGraph);});
        graphs.put(name, newGraph);
    }

    @Override
    public InterdependencyGraph<Unit> getNamedGraph(String name) {
        return graphs.get(name);
    }
}
