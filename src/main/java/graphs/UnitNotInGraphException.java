package graphs;

import java.util.LinkedList;
import java.util.List;

public class UnitNotInGraphException extends RuntimeException{

    private List<String> nodes;

    public UnitNotInGraphException(String... node){
        List<String> nodes = new LinkedList<>();
        for( String n : node ){
            nodes.add(n);
        }
        this.nodes = nodes;
    }

    public UnitNotInGraphException(List<String> node){
        this.nodes = node;
    }

    @Override
    public String getMessage() {
        return String.format("Unable to find following units in the graph: "+nodes);
    }
}
