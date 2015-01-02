package it.celi.orient.util;

/**
 * Created by Riccardo Tasso on 17/12/14.
 */
public class Joiner {

    public final static com.google.common.base.Joiner listJoiner = com.google.common.base.Joiner.on(", ");
//    public final static com.google.common.base.Joiner oneSpaceJoiner = com.google.common.base.Joiner.on(" ");
    public final static com.google.common.base.Joiner andJoiner = com.google.common.base.Joiner.on(" AND ");
    public final static com.google.common.base.Joiner orJoiner = com.google.common.base.Joiner.on(" OR ");

}
