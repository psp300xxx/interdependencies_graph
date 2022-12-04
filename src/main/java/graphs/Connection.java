package graphs;

public interface Connection<T extends  Unit> {

    T getFrom();

    T getTo();

    Double weight();

}
