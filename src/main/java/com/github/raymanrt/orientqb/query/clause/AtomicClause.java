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

import com.github.raymanrt.orientqb.query.Assignable;
import com.github.raymanrt.orientqb.query.Clause;
import com.github.raymanrt.orientqb.query.Operator;
import com.github.raymanrt.orientqb.query.Projection;
import com.github.raymanrt.orientqb.util.Commons;

import static java.lang.String.format;

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