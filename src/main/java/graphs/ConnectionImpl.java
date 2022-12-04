package graphs;

public class ConnectionImpl implements Connection<Unit>{
    private Unit from, to;
    private Double weight;

    public ConnectionImpl(Unit from, Unit to, Double weight){
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public ConnectionImpl(Unit from, Unit to){
        this(from, to, null);
    }

    @Override
    public Unit getFrom() {
        return from;
    }

    @Override
    public Unit getTo() {
        return to;
    }

    @Override
    public Double weight() {
        return weight;
    }
}
