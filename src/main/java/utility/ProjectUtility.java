package utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import graphs.InterdependencyGraph;
import graphs.Unit;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProjectUtility {

    private static ObjectMapper mapper;

    public static ObjectMapper getMapper(){
        if(mapper==null){
            mapper = new ObjectMapper();
        }
        return mapper;
    }

    private ProjectUtility(){

    }

    public static <T> Collection<T> copyCollection(Collection<T> original){
        List<T> result = new ArrayList<>();
        for( T elem : original ){
            result.add(elem);
        }
        return result;
    }

    public static boolean closeEnough(double a, double b){
        return closeEnough(a, b, 1E-8);
    }

    public static boolean closeEnough(double a, double b, double eps){
        double diff = a - b;
        return Math.abs(diff) < eps;
    }

    public static void writeGraphToFile(InterdependencyGraph graph, String filepath) throws IOException {
        String jsonValue = getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(graph);
        writeStringtoFile(filepath, jsonValue);
    }

    public static boolean existsConnection(Unit a, Unit b, boolean firstToSecond){
        Unit first = a;
        Unit second = b;
        if(!firstToSecond){
            first = b;
            second = a;
        }
        boolean result = first.getConnections(false).contains(second) && second.getConnections(true).contains(first);
        return result;
    }

    public static void writeStringtoFile(String path, String jsonString) throws IOException{
        File file = new File(path);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(jsonString.getBytes(StandardCharsets.UTF_8));
        fos.flush();
        fos.close();
    }

}
