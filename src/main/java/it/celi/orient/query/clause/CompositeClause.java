package it.celi.orient.query.clause;

import com.google.common.base.Joiner;
import it.celi.orient.query.Clause;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;
import static it.celi.orient.util.Commons.whereToStringFunction;

/**
 * Created by rayman on 19/12/14.
 */
public class CompositeClause extends Clause {
    private final Joiner joiner;
    private final Clause[] clauses;

    public CompositeClause(Joiner joiner, Clause... clauses) {
        this.joiner = joiner;
        this.clauses = clauses;
    }

    public String toString() {
        List<String> clausesList = transform(newArrayList(clauses), whereToStringFunction);
        return joiner.join(clausesList);
    }
}