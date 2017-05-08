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

package com.github.raymanrt.orientqb.query.clause;

import com.github.raymanrt.orientqb.query.Clause;
import com.google.common.base.Joiner;

import java.util.List;

import static com.github.raymanrt.orientqb.util.Commons.whereToStringFunction;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;

public class CompositeClause extends Clause {
    private final Joiner joiner;
    private final Clause[] clauses;

    public CompositeClause(Joiner joiner, Clause... clauses) {
        this.joiner = joiner;
        this.clauses = clauses;
    }

    @Override
    public String toString() {
        List<String> clausesList = transform(newArrayList(clauses), whereToStringFunction);
        return joiner.join(clausesList);
    }
}