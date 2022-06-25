package graphs;

import java.util.*;

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

    @Override
    public double getState(Set<Unit> visitedUnit) {
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
    public double getUnitState() {
        return state;
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof AloneUnit)){
            return false;
        }
        if( obj == this ){
            return true;
        }
        AloneUnit other = (AloneUnit) obj;
        return other.getName().equals(this.getName());
    }

    @Override
    public String toString() {
        return String.format("[AloneUnit] Name=%s, State=%s", getName(), getState());
    }

    @Override
    public List<Unit> getConnections() {
        if(connections==null){
            connections = new ArrayList<>();
        }
        return Collections.unmodifiableList(connections);
    }
}
