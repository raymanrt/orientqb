package it.raymanrt.orient.query.clause;

import com.google.common.base.Joiner;
import it.raymanrt.orient.query.Clause;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;
import static it.raymanrt.orient.util.Commons.whereToStringFunction;

/**
 * Created by Riccardo Tasso on 19/12/14.
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