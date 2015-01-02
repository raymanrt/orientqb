package it.celi.orient.query.projection;

import it.celi.orient.query.Projection;

/**
 * Created by Riccardo Tasso on 21/12/14.
 */
public class AtomicProjection extends Projection {
    private final String field;

    public AtomicProjection(String field) {
        super();
        this.field = field;
    }

    public String toString() {
        String string = field;
        if(alias.isPresent())
            string += " as " + alias.get();
        return string;
    }

    @Override
    public String getAssignment() {
        return field;
    }
}
