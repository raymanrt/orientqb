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

public class CompositeProjection extends Projection {
    private Projection[] projections;

    private String format;

    public CompositeProjection(String format, Projection ... projections) {
        super();
        this.format = format;
        this.projections = projections;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(getAssignment());
        if(alias.isPresent()) {
            builder.append(" as ").append(alias.get());
        }
        return builder.toString();
    }

    @Override
    public String getAssignment() {
        String[] assignments = getAssignments(projections);
        return String.format(format, assignments);
    }

    private String[] getAssignments(Projection[] projections) {
        String[] ret = new String[projections.length];
        for(int i = 0; i < projections.length; i ++) {
            ret[i] = projections[i].getAssignment();
        }
        return ret;
    }
}