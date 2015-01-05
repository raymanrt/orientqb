package it.raymanrt.orient.query.clause;

import it.raymanrt.orient.query.Assignable;
import it.raymanrt.orient.query.Clause;
import it.raymanrt.orient.query.Operator;
import it.raymanrt.orient.query.Projection;
import it.raymanrt.orient.util.Commons;

import static java.lang.String.format;

/**
 * Created by Riccardo Tasso on 19/12/14.
 */
public class AtomicClause extends Clause {
    private final Object firstOperand;
    private final Operator operator;
    private final Object value;

    public AtomicClause(String field, Operator operator, Object value) {
        this.firstOperand = field;
        this.operator = operator;
        this.value = value;
    }

    public AtomicClause(Projection projection, Operator operator, Object value) {
        this.firstOperand = projection;
        this.operator = operator;
        this.value = value;
    }

    public String toString() {
        String valueString = Commons.cast(value);
        if(value instanceof Assignable) {
            Assignable assignable = (Assignable) value;
            valueString = assignable.getAssignment();
        }

        String firstOperandString = firstOperand.toString();
        if(firstOperand instanceof Assignable) {
            Assignable assignable = (Assignable) firstOperand;
            firstOperandString = assignable.getAssignment();
        }

        return format(operator.getFormat(), firstOperandString, valueString);
    }
}