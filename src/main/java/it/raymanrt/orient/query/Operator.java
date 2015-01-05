package it.raymanrt.orient.query;

/**
 * Created by Riccardo Tasso on 19/12/14.
 */
public enum Operator {

    // unary
    EQ("%s = %s"),
    NE("%s <> %s"),
    LT("%s < %s"),
    LE("%s <= %s"),
    GT("%s > %s"),
    GE("%s >= %s"),
    LIKE("%s LIKE %s"),

    INSTANCEOF("%s INSTANCEOF %s"),
    IN("%s IN %s"),
    CONTAINS_KEY("%s CONTAINSKEY %s"),
    CONTAINS_VALUE("%s CONTAINSVALUE %s"),
    CONTAINS_TEXT("%s CONTAINSTEXT %s"),
    MATCHES("%s MATCHES %s"),

    LUCENE("%s LUCENE %s"),

    NEAR("%s NEAR %s"),
    WITHIN("%s WITHIN %s"),

    // TRAVERSE is deprecated

    // binary
    BETWEEN("%s BETWEEN %s AND %s"),
    // TODO: CONTAINS("%s CONTAINS %s"),
    // TODO: CONTAINS_ALL("%s CONTAINSALL (%s = %s)"), // TODO: others operators supported?


    // TODO: these are not operators, but should be treated the same
    FIELD("%s[%s = %s]"),

    NOT("NOT(%s)")

    ;


    private final String format;

    private Operator(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    @Override
    public String toString() {
        return format;
    }
}