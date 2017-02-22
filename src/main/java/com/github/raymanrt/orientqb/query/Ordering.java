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

public class Ordering {
    private final Projection projection;
    private final Order order;

    public enum Order {
        ASC,
        DESC
    }

    public Ordering(Projection projection, Order order) {
        this.projection = projection;
        this.order = order;
    }

    public String toString() {
        return projection.toString() + " " + order.toString();
    }

    public Projection getProjection() {
        return this.projection;
    }

    @Override
    public int hashCode() {
        return this.projection.toString().hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Ordering)) {
            return false;
        }

        Ordering otherOrder = (Ordering) other;

        return this.getProjection().toString().equals(otherOrder.getProjection().toString());
    }
}
