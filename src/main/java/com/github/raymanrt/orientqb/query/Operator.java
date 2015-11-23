/*
 * Copyright 2015 Riccardo Tasso
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.raymanrt.orientqb.query;

import com.github.raymanrt.orientqb.util.Token;

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
    CONTAINS("%s " + Token.CONTAINS + " %s"),
    CONTAINS_KEY("%s CONTAINSKEY %s"),
    CONTAINS_VALUE("%s CONTAINSVALUE %s"),
    CONTAINS_TEXT("%s CONTAINSTEXT %s"),
    MATCHES("%s " + Token.MATCHES + " %s"),

    LUCENE("%s LUCENE %s"),

    NEAR("%s NEAR %s"),
    WITHIN("%s WITHIN %s"),

    // TRAVERSE is deprecated

    // binary
    BETWEEN("%s BETWEEN %s AND %s"),
    // TODO: CONTAINS_ALL("%s CONTAINSALL (%s = %s)"), // TODO: others operators supported?


    // TODO: these are not operators, but should be treated the same
    FIELD("%s[%s = %s]"),

    NOT(Token.NOT + " (%s)"),
    NOT_WITHOUT_PARENTHESIS(Token.NOT + " %s"),

    DEFINED("%s IS DEFINED"),
    NOT_DEFINED("%s IS NOT DEFINED"),

    NULL("%s IS " + Token.NULL),
    NOT_NULL("%s IS " + Token.NOT + " " + Token.NULL)

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