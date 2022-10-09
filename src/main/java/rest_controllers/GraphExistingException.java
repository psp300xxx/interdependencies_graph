package rest_controllers;

public class GraphExistingException extends Exception{

    private String name;

    public GraphExistingException(String name){
        this.name = name;
    }

    @Override
    public String getMessage() {
        return String.format("Graph having name='%s' already exists", name);
    }
}
