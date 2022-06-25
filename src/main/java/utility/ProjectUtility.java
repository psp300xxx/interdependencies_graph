package utility;

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
        return closeEnough(a, b, 1E-6);
    }

    public static boolean closeEnough(double a, double b, double eps){
        double diff = a - b;
        return Math.abs(diff) < eps;
    }

}
