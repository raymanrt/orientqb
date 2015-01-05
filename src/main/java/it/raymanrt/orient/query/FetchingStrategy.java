package it.raymanrt.orient.query;

import it.raymanrt.orient.query.projection.AtomicProjection;

/**
 * Created by Riccardo Tasso on 30/12/14.
 */
public class FetchingStrategy {
    public static int ONLY_CURRENT = 0;
    public static int UNLIMITED = -1;
    public static int EXCLUDE_CURRENT = -2;

    private Projection fieldPath;
    private int depthLevel;

    public FetchingStrategy(Projection fieldPath, int depthLevel) {
        this.fieldPath = fieldPath;
        this.depthLevel = depthLevel;
    }

    public FetchingStrategy(String fieldName, int depthLevel) {
        this.fieldPath = new AtomicProjection(fieldName);
        this.depthLevel = depthLevel;
    }

    public String toString() {
        return fieldPath.toString() + ":" + Integer.toString(depthLevel);
    }

}