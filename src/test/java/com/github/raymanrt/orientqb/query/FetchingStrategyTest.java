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

import com.github.raymanrt.orientqb.query.fetchingstrategy.FetchingStrategy;
import com.github.raymanrt.orientqb.query.fetchingstrategy.Level;
import org.junit.Test;

import static com.github.raymanrt.orientqb.query.Projection.projection;
import static com.github.raymanrt.orientqb.query.Variable.thisRecord;
import static org.junit.Assert.assertEquals;

/**
 * Created by rayman on 26/05/16.
 */
public class FetchingStrategyTest {
    @Test
    public void compositeTest() {

        FetchingStrategy strategy = FetchingStrategy.composite(
                new FetchingStrategy(projection("in_*"), -2),
                new FetchingStrategy(projection("out_*"), -2)
        );
        Projection p = projection("field").toJson(strategy, JsonFormat.VERSION, JsonFormat.RID);
        assertEquals("field.toJson('fetchPlan:in_*:-2 out_*:-2,version,rid')", p.toString());


    }

    @Test
    public void simpleTest() {
        FetchingStrategy strategy = new FetchingStrategy("customer", 1);
        Projection p = projection("invoice").toJson(strategy);
        assertEquals("invoice.toJson('fetchPlan:customer:1')", p.toString());


    }

    @Test
    public void anotherCompositeTest() {
        FetchingStrategy strategy = FetchingStrategy.composite(
                new FetchingStrategy("customer", 1),
                new FetchingStrategy("orders", 2)
        );
        Projection p = projection("invoice").toJson(strategy);
        assertEquals("invoice.toJson('fetchPlan:customer:1 orders:2')", p.toString());


    }

    @Test
    public void wildcardTest() {
        FetchingStrategy strategy = new FetchingStrategy(Projection.ALL, 3);
        Projection p = projection("invoice").toJson(strategy);
        assertEquals("invoice.toJson('fetchPlan:*:3')", p.toString());


    }

    @Test
    public void queryTest() {
        FetchingStrategy strategy = new FetchingStrategy("out_Friend", 4);
        Query q = new Query()
                .select(thisRecord().toJson(strategy))
                .from("#10:20");
        assertEquals("SELECT @this.toJson('fetchPlan:out_Friend:4') FROM #10:20", q.toString());


    }

    @Test
    public void queryWildcardTest() {
        FetchingStrategy strategy = new FetchingStrategy("in_*", -2);
        Query q = new Query()
                .select(thisRecord().toJson(strategy))
                .from("#10:20");
        assertEquals("SELECT @this.toJson('fetchPlan:in_*:-2') FROM #10:20", q.toString());


    }

    @Test
    public void queryWithLevelTest() {
        FetchingStrategy strategy = new FetchingStrategy(Level.level(1), "field", 5);
        Query q = new Query()
                .select(thisRecord().toJson(strategy))
                .from("#10:20");
        assertEquals("SELECT @this.toJson('fetchPlan:[1]field:5') FROM #10:20", q.toString());


    }

    @Test
    public void queryWithRangeLevelTest() {
        FetchingStrategy strategy = new FetchingStrategy(Level.level(1, 5), "field", 5);
        Query q = new Query()
                .select(thisRecord().toJson(strategy))
                .from("#10:20");
        assertEquals("SELECT @this.toJson('fetchPlan:[1-5]field:5') FROM #10:20", q.toString());


    }

    @Test
    public void queryWithFromRangeTest() {
        FetchingStrategy strategy = new FetchingStrategy(Level.fromLevel(2), "field", 5);
        Query q = new Query()
                .select(thisRecord().toJson(strategy))
                .from("#10:20");
        assertEquals("SELECT @this.toJson('fetchPlan:[2-]field:5') FROM #10:20", q.toString());


    }

    @Test
    public void queryWithTORangeTest() {
        FetchingStrategy strategy = new FetchingStrategy(Level.toLevel(2), "field", 5);
        Query q = new Query()
                .select(thisRecord().toJson(strategy))
                .from("#10:20");
        assertEquals("SELECT @this.toJson('fetchPlan:[-2]field:5') FROM #10:20", q.toString());


    }

    @Test
    public void queryWithAnyLevelTest() {
        FetchingStrategy strategy = new FetchingStrategy(Level.anyLevel(), "field", 5);
        Query q = new Query()
                .select(thisRecord().toJson(strategy))
                .from("#10:20");
        assertEquals("SELECT @this.toJson('fetchPlan:[*]field:5') FROM #10:20", q.toString());

    }
}
