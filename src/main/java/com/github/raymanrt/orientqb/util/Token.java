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

import com.google.common.collect.Sets;

import java.lang.reflect.Field;
import java.util.Set;

public class Token {

    public static final String SELECT = "SELECT";
    public static final String TRAVERSE = "TRAVERSE";
    public static final String INSERT = "INSERT";
    public static final String UPDATE = "UPDATE";
    public static final String DELETE = "DELETE";

    public static final String LET = "LET";
    public static final String FROM = "FROM";
    public static final String WHERE = "WHERE";
    public static final String GROUP_BY = "GROUP BY";
    public static final String ORDER_BY = "ORDER BY";
    public static final String SKIP = "SKIP";
    public static final String LIMIT = "LIMIT";

    public static final String INTO = "INTO";
    public static final String VALUES = "VALUES";
    public static final String SET = "SET";
    public static final String ADD = "ADD";
    public static final String REMOVE = "REMOVE";

    public static final String RANGE = "RANGE";
    public static final String ASC = "ASC";
    public static final String DESC = "DESC";
    public static final String AS = "AS";
    public static final String THIS = "THIS";


    public static final String NULL = "NULL";

    public static final String FETCHPLAN = "FETCHPLAN";
    public static final String TIMEOUT = "TIMEOUT";
    public static final String LOCK = "LOCK";
    public static final String PARALLEL = "PARALLEL";

    // Operators
    public static final String CONTAINS = "CONTAINS";
    public static final String MATCHES = "MATCHES";

    public static final String AND = "AND";
    public static final String OR = "OR";
    public static final String NOT = "NOT";

    public static final Set<String> tokens() {
        Set<String> tokens = Sets.newHashSet();
        for(Field field : Token.class.getFields()) {
            tokens.add(field.getName());
        }
        return tokens;
    }
}
