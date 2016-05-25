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

import com.github.raymanrt.orientqb.query.projection.AtomicProjection;

public class FetchingStrategy {
    public static Projection ALL_LEVELS = Projection.projection("[*]");
    public static int ONLY_CURRENT = 0;
    public static int UNLIMITED = -1;
    public static int EXCLUDE_CURRENT = -2;

    private Projection levels;
    private Projection fieldPath;
    private int depthLevel;

    public FetchingStrategy(Projection fieldPath, int depthLevel) {
        this.fieldPath = fieldPath;
        this.depthLevel = depthLevel;
    }

    public FetchingStrategy(String fieldName, int depthLevel) {
        this.fieldPath = new AtomicProjection(fieldName);
        this.depthLevel = depthLevel;
    }

    public FetchingStrategy(Projection levels, Projection fieldPath, int depthLevel) {
        this.levels = levels;
        this.fieldPath = fieldPath;
        this.depthLevel = depthLevel;
    }

    public FetchingStrategy(Projection levels, String fieldName, int depthLevel) {
        this.levels = levels;
        this.fieldPath = new AtomicProjection(fieldName);
        this.depthLevel = depthLevel;
    }

    public FetchingStrategy(String levelsName, Projection fieldPath, int depthLevel) {
        this.levels = new AtomicProjection(levelsName);
        this.fieldPath = fieldPath;
        this.depthLevel = depthLevel;
    }

    public FetchingStrategy(String levelsName, String fieldName, int depthLevel) {
        this.levels = new AtomicProjection(levelsName);
        this.fieldPath = new AtomicProjection(fieldName);
        this.depthLevel = depthLevel;
    }

    public String toString() {
        if (levels != null) {
            return levels.toString() + fieldPath.toString() + ":" + Integer.toString(depthLevel);
        }
        return fieldPath.toString() + ":" + Integer.toString(depthLevel);
    }

}
