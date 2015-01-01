package it.celi.orient.query;

/**
 * Created by rayman on 30/12/14.
 */
public class Parameter {

    private final String name;

    public Parameter(String name) {
        this.name = name;
    }

    public static final Parameter PARAMETER = new Parameter("?");

    public static Parameter parameter(String name) {
        return new Parameter(":" + name);
    }

    public String toString() {
        return name;
    }
}
