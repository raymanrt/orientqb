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

import static com.github.raymanrt.orientqb.util.Joiner.j;

public class Variable {

    private Variable() {};

    public static Projection variable(String name) {
        return new AtomicProjection(j.join("$", name));
    }

    public static Projection thisRecord() {
        return new AtomicProjection("@this");
    }

    public static Projection rid() {
        return new AtomicProjection("@rid");
    }

    public static Projection classRecord() {
        return new AtomicProjection("@class");
    }

    public static Projection version() {
        return new AtomicProjection("@version");
    }

    public static Projection size() {
        return new AtomicProjection("@size");
    }

    public static Projection type() {
        return new AtomicProjection("@type");
    }

    public static Projection parent() {
        return new AtomicProjection("$parent");
    }

    public static Projection current() {
        return new AtomicProjection("$current");
    }

    public static Projection distance() {
        return new AtomicProjection("$distance");
    }
}
