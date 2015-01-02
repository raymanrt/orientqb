package it.celi.orient.query.projection;

import it.celi.orient.query.Projection;

/**
 * Created by Riccardo Tasso on 21/12/14.
 */
public class CompositeProjection extends Projection {
    private Projection[] projections;

    private String format;

    public CompositeProjection(String format, Projection ... projections) {
        super();
        this.format = format;
        this.projections = projections;
    }

    public String toString() {
        String string = getAssignment();
        if(alias.isPresent())
            string += " as " + alias.get();
        return string;
    }

    @Override
    public String getAssignment() {
        String[] assignments = getAssignments(projections);
        return String.format(format, assignments);
    }

    private String[] getAssignments(Projection[] projections) {
        String[] ret = new String[projections.length];
        for(int i = 0; i < projections.length; i ++) {
            ret[i] = projections[i].getAssignment();
        }
        return ret;
    }
}