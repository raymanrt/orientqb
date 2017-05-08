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

package com.github.raymanrt.orientqb.util;

import com.github.raymanrt.orientqb.query.Clause;
import com.github.raymanrt.orientqb.query.clause.CompositeClause;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Lists.newArrayList;

public class Commons {

    private Commons() {};

    public static final Function<Clause, String> whereToStringFunction = new Function<Clause, String>() {
        @Override
        public String apply(Clause input) {
            String string = input.toString();
            if(input instanceof CompositeClause)
                string = "( " + string + " )";
            return string;
        }
    };
    public static final Function<Object, String> toStringFunction = new Function<Object, String>() {
        @Override
        public String apply(Object input) {
            return input.toString();
        }
    };
    public static final Function<String, String> singleQuoteFunction = new Function<String, String>() {
        @Override
        public String apply(String input) {
            return singleQuote(input);
        }
    };
    private final static Function<Object, Object> castFunction = new Function<Object, Object>() {
        @Override
        public Object apply(Object input) {
            return cast(input);
        }
    };

    @SuppressWarnings("unchecked")
    public static String cast(Object value) {
        if(value instanceof String) {
            return singleQuote((String) value);
        }
        if(value instanceof Collection) {
            Collection<Object> collection = (Collection<Object>) value;
            collection = transform(collection, castFunction);
            return collectionToString(collection);
        }
        return value.toString();
    }

    public static String joinStrings(String ... strings) {
        List<String> tokens = newArrayList(strings);
        tokens = Lists.transform(tokens, singleQuoteFunction);
        return Joiner.listJoiner.join(tokens);
    }

    public static String singleQuote(String input) {
        return "'" + input.replace("'", "\\'") + "'";
    }

    public static String collectionToString(Collection<Object> collection) {
        return "[" + Joiner.listJoiner.join(collection) + "]";
    }

    public static String arrayToString(Object ... array) {
        return "[" + Joiner.listJoiner.join(array) + "]";
    }

    public static String clean(String query) {
        return Patterns.manyWhiteSpaces.matcher(query).replaceAll(" ").trim();
    }

    public static boolean validVariable(String variable) {
        boolean isValid = variable.startsWith(":");
        isValid = isValid && !(variable.contains(Patterns.WHITESPACE));
        return isValid;
    }
}
