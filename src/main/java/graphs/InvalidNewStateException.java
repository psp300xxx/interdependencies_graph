package graphs;

public class InvalidNewStateException extends RuntimeException{

    private Unit unit;

    private Double state;

    public InvalidNewStateException(Unit unit, Double state){
        this.unit = unit;
        this.state = state;
    }

    @Override
    public String getMessage() {
        return String.format( "New state is not valid, unit='%s', state='%s'", unit.getName(), state );
    }
}
