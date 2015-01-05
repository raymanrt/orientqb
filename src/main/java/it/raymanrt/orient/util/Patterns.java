package it.raymanrt.orient.util;

import java.util.regex.Pattern;

/**
 * Created by Riccardo Tasso on 28/12/14.
 */
public class Patterns {
    public static final Pattern IDENTIFIER = Pattern.compile("([a-zA-Z_]+|[*])");
    public static final CharSequence WHITESPACE = " ";
    static final Pattern manyWhiteSpaces = Pattern.compile("\\s+");
}
