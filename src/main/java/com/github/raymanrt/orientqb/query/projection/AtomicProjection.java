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

import java.util.HashSet;
import java.util.Set;

public class AtomicProjection extends Projection {
    private final String field;

    private static Set<String> reserved = new HashSet<String>();

    static {
        reserved.add("select");
        reserved.add("traverse");
        reserved.add("insert");
        reserved.add("update");
        reserved.add("delete");
        reserved.add("from");
        reserved.add("where");
        reserved.add("skip");
        reserved.add("limit");
        reserved.add("timeout");
        reserved.add("contains");
        reserved.add("match");
        reserved.add("into");
        reserved.add("values");
        reserved.add("set");
        reserved.add("add");
        reserved.add("remove");
        reserved.add("and");
        reserved.add("or");
        reserved.add("null");
        reserved.add("order");
        reserved.add("by");
        reserved.add("limit");
        reserved.add("range");
        reserved.add("asc");
        reserved.add("dessc");
        reserved.add("as");
        reserved.add("this");
    }

    public AtomicProjection(String field) {
        super();
        if (reserved.contains(field.toLowerCase())){
            this.field = "`" + field + "`";
        } else {
            this.field = field;
        }
    }

    public String toString() {
        String string = field;
        if(alias.isPresent())
            string += " as " + alias.get();
        return string;
    }

    @Override
    public String getAssignment() {
        return field;
    }

}
