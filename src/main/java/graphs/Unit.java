package graphs;

public interface Unit {

    Graph<Unit> graph();

    void setDelegate(UnitDelegate unitDelegate);

    String getName();

    void sendUpdate(UnitMessage message);

    double getState();

    void setGraph(Graph graph);

    void setState(Double newState);

    void receiveMessage(UnitMessage message);

}
