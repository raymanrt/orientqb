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

package com.github.raymanrt.orientqb.query.projection;

import com.github.raymanrt.orientqb.query.Projection;
import com.github.raymanrt.orientqb.util.Token;

import static com.github.raymanrt.orientqb.util.Joiner.j;

public class AtomicProjection extends Projection {
    private final String field;

    public AtomicProjection(String field) {
        super();
        if (Token.tokens().contains(field.toUpperCase())){
            this.field = j.join("`", field, "`");
        } else {
            this.field = field;
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(field);
        if(alias.isPresent()) {
            builder.append(" as ").append(alias.get());
        }
        return builder.toString();
    }

    @Override
    public String getAssignment() {
        return field;
    }

}
