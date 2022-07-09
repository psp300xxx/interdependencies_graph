package utility;

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

    public static void writeJSONtoFile(String path, JSONObject jsonObject) throws IOException{
        writeJSONtoFile(path, jsonObject, 4);
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

    public static void writeJSONtoFile(String path, JSONObject jsonObject, int indentFactor) throws IOException{
        File file = new File(path);
        String jsonString = jsonObject.toString(indentFactor);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(jsonString.getBytes(StandardCharsets.UTF_8));
        fos.flush();
        fos.close();
    }

}
