package graphs;

public interface UnitMessage {

    Unit getSource();

    Unit getDestination();

    void setDestination(Unit to);

    Double getConnectionWeight();

    void setConnectionWeight(Double weight);

    UnitMessage copy();

}
