package it.celi.orient.query.clause;

import it.celi.orient.query.Clause;
import it.celi.orient.query.Operator;

import static java.lang.String.format;

/**
 * Created by Riccardo Tasso on 28/12/14.
 */
public class CustomFormatClause extends Clause {

    private final Operator operator;
    private final Object[] clauses;

    public CustomFormatClause(Operator operator, Object... clauses) {
        this.operator = operator;
        this.clauses = clauses;
    }

    public String toString() {
        return format(operator.getFormat(), clauses);
    }


}
