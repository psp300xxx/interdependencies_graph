package graphs;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import graphs.publish_subscribe.UnitDelegate;
import graphs.publish_subscribe.UnitManagerThread;
import graphs.publish_subscribe.UnitSubscriber;
import org.json.JSONObject;
import utility.ProjectUtility;

import java.util.List;
import java.util.Set;
@JsonIgnoreProperties(value = { "delegate" })
public interface Unit extends UnitSubscriber {


    UnitManagerThread getThread();

    UnitDelegate getDelegate();

    void setDelegate(UnitDelegate delegate);

    void addConnection(Unit toUnit, boolean incoming);


    default void addConnection(Unit toUnit, boolean incoming, boolean setOpposite){
        if(ProjectUtility.existsConnection(this, toUnit, incoming)){
            return;
        }
        this.addConnection(toUnit, incoming);
        if(setOpposite){
            toUnit.addConnection(this, !incoming);
        }
    }

    void setUnitManagerThread( UnitManagerThread newThread );

    @JsonGetter("name")
    String getName();

    @JsonGetter("state")
    double getState();

    default String getNodesConnectionsString(){
        StringBuilder sb = new StringBuilder();
        List<Unit> inbounds = getConnections(true);
        if(inbounds.isEmpty()){
            sb.append("{}->"+getName()+"\n");
        }
        else{
            sb.append('{');
            for( Unit unit:inbounds ){
                sb.append(unit.getName()+", ");
            }
            sb.setLength(sb.length()-2);
            sb.append("}->"+getName()+"\n");
        }
        List<Unit> outbounds = getConnections(false);
        if(outbounds.isEmpty()){
            sb.append(getName()+"->{}\n");
        }
        else{
            sb.append(getName()+"->{");
            for( Unit unit:outbounds ){
                sb.append(unit.getName()+", ");
            }
            sb.setLength(sb.length()-2);
            sb.append('}');
        }
        return sb.toString();
    }

    double getUnitState();

    @JsonGetter("inboundConnections")
    default List<Unit> getInboundConnections(){
        return getConnections(true);
    }

    @JsonGetter("outboundConnections")
    default List<Unit> getOutboundConnections(){
        return getConnections(false);
    }

    List<Unit> getConnections(boolean incoming);

    default String getConnectionsAsString(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s -> {", getName()));
        if(getConnections(false).isEmpty()){
            sb.append('}');
            return sb.toString();
        }
        for( Unit unit : getConnections(false) ){
            sb.append(unit.getName()+", ");
        }
        sb.setLength(sb.length()-2);
        sb.append('}');
        return sb.toString();
    }


}
