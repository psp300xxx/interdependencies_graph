package main;

import graphs.InterdependencyGraph;
import graphs.Unit;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import rest_controllers.GraphExistingException;
import rest_controllers.GraphManager;
import rest_controllers.GraphManagerDelegate;
import rest_controllers.GraphManagerImpl;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MainRestController implements GraphManagerDelegate {

    private static final String HELLO_MESSAGE = "Hi, this is the interdependencies graph" +
            "application, in order to help you understand how the climate clock works!";

    private GraphManager graphManager = new GraphManagerImpl();

    @GetMapping("/")
    public String hello(){
        return HELLO_MESSAGE;
    }

    @PostMapping(value = "/createNewGraph", consumes = {"application/json"})
    public Map<String, String> createNewGraph(@RequestBody Map<String, String> input){
        String graphName = input.get("name");
        Map<String, String> result = new HashMap<>();
        try {
            graphManager.createNewGraph(graphName);
            result.put("message", String.format("Graph with name='%s' correctly created", graphName));
            return result;
        } catch (GraphExistingException e) {
            result.put("error", e.getMessage());
            return result;
        }
    }

    @PostMapping(value = "/addNodeToGraph", consumes = {"application/json"})
    public Map<String, String> addNodeToGraph(@RequestBody Map<String, Object> input){
        String graph = (String)input.get("graph");
        Map<String, String> response = new HashMap<>();
        InterdependencyGraph<Unit> currentGraph = graphManager.getNamedGraph(graph);
        if(graph==null || currentGraph==null){
            response.put("error", String.format("Graph named='%s' not found", graph));
            return response;
        }
        String fromNode = (String) input.get("source");
        Unit sourceNode = currentGraph.getNamedNode(fromNode);
        if(sourceNode==null){
            response.put("error", String.format("Node named='%s' in graph='%s' not found", fromNode, graph));
            return response;
        }
        String toNode = (String) input.get("destination");
        Unit destinationNode = currentGraph.getNamedNode(toNode);
        if(destinationNode!=null){
            sourceNode.addConnection(destinationNode, false);
        }
//        Subroutine to create node from JSON
//        TODO: COMPLETE!
        return null;
    }

    @GetMapping("/error")
    public void error(){

    }

    @Override
    public void graphCreated(GraphManager manager, InterdependencyGraph<Unit> newGraph) {

    }

    @Override
    public void graphCreationFailed(GraphManager manager, String name, Exception exception) {

    }
}
