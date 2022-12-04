package graphs;

public class GraphIsRunningException extends Exception{

    private Graph graph;

    public GraphIsRunningException(Graph graph){
        this.graph = graph;
    }

    public Graph getGraph() {
        return graph;
    }

    @Override
    public String getMessage() {
        return String.format("Graph='%s' is running, unable to operate on it", graph.getName());
    }
}
