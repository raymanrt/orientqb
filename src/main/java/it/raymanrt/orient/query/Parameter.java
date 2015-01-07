package it.raymanrt.orient.query;

/**
 * Created by Riccardo Tasso on 30/12/14.
 */
public class Parameter extends Projection {

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

    @Override
    public String getAssignment() {
        return toString();
    }
}