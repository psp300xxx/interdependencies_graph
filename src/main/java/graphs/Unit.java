package graphs;

import java.util.List;
import java.util.Set;

public interface Unit {

    String getName();
    double getState();

    double getState(Set<Unit> visitedUnit);

    double getUnitState();

    List<Unit> getConnections();

    default String getConnectionsAsString(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s -> {", getName()));
        if(getConnections().isEmpty()){
            sb.append('}');
            return sb.toString();
        }
        for( Unit unit : getConnections() ){
            sb.append(unit.getName()+", ");
        }
        sb.setLength(sb.length()-2);
        sb.append('}');
        return sb.toString();
    }


}
