package utility;

public enum StringConstants {

    WELCOME_MESSAGE("Hi, welcome to the Climate Clock POC application!"),

    ERROR_GENERAL_MESSAGE("Unable to process the request, unexpected error occured");

    StringConstants(String value){
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }
}
