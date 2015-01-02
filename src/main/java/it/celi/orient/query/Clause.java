package it.celi.orient.query;

import it.celi.orient.query.clause.AtomicClause;
import it.celi.orient.query.clause.CompositeClause;
import it.celi.orient.query.clause.CustomFormatClause;
import it.celi.orient.util.Joiner;

import static it.celi.orient.query.Operator.NOT;

/**
 * Created by Riccardo Tasso on 19/12/14.
 */
public class Clause {

    public static final Clause clause(String field, Operator operator, Object value) {
        return new AtomicClause(field, operator, value);
    }

    public static final Clause clause(Projection projection, Operator operator, Object value) {
        return new AtomicClause(projection, operator, value);
    }

    public static final Clause and(Clause ... clauses) {
        return new CompositeClause(Joiner.andJoiner, clauses);
    }

    public static final Clause not(Clause clause) {
        return new CustomFormatClause(NOT, clause);
    }

    public static final Clause or(Clause ... clauses) {
        return new CompositeClause(Joiner.orJoiner, clauses);
    }
}