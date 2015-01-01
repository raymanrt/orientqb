package it.celi.orient.query;

import it.celi.orient.query.projection.AtomicProjection;

/**
 * Created by rayman on 28/12/14.
 */
public class Variable {

    public static Projection variable(String name) {
        return new AtomicProjection("$" + name);
    }

    public static Projection thisRecord() {
        return new AtomicProjection("@this");
    }

    public static Projection rid() {
        return new AtomicProjection("@rid");
    }

    public static Projection classRecord() {
        return new AtomicProjection("@class");
    }

    public static Projection version() {
        return new AtomicProjection("@version");
    }

    public static Projection size() {
        return new AtomicProjection("@size");
    }

    public static Projection type() {
        return new AtomicProjection("@type");
    }

    public static Projection parent() {
        return new AtomicProjection("$parent");
    }

    public static Projection current() {
        return new AtomicProjection("$current");
    }

    public static Projection distance() {
        return new AtomicProjection("$distance");
    }
}
