package graphs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AloneUnit implements Unit{

    private double state;
    private String name;

    private List<Unit> connections;

    public AloneUnit(String name){
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getState() {
        return state;
    }

    public void setState(double newState){
        this.state = newState;
    }

    public void addConnections(Collection<Unit> connections){
        for( Unit connection : connections ){
            addConnection(connection);
        }
    }

    public void addConnection(Unit newConnection){
        if(connections==null){
            connections = new ArrayList<>();
        }
        connections.add(newConnection);
    }

    @Override
    public List<Unit> getConnections() {
        if(connections==null){
            connections = new ArrayList<>();
        }
        return Collections.unmodifiableList(connections);
    }
}
