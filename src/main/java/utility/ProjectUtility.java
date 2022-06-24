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

}
