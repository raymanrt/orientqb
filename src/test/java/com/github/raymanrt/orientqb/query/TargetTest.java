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

import org.junit.Test;

import static com.github.raymanrt.orientqb.query.Projection.projection;
import static com.github.raymanrt.orientqb.query.ProjectionFunction.sum;
import static org.junit.Assert.assertEquals;

public class TargetTest {

	@Test
	public void simpleTest() {
		Target t = Target.target("Class");
		assertEquals("Class", t.toString());
	}

	@Test
	public void multipleTest() {
		Target t = Target.target("#1:1", "#2:2");
		assertEquals("[#1:1, #2:2]", t.toString());
	}

	@Test
	public void clusterTest() {
		Target t = Target.cluster(5);
		assertEquals("cluster:5", t.toString());
	}

    @Test
    public void namedClusterTest() {
        Target t = Target.cluster("person");
        assertEquals("cluster:person", t.toString());
    }

	@Test
	public void indexValuesTest() {
		Target t = Target.indexValues("myIndex");
		assertEquals("indexvalues:myIndex", t.toString());
	}

	@Test
	public void indexValuesAscTest() {
		Target t = Target.indexValuesAsc("myIndex");
		assertEquals("indexvaluesasc:myIndex", t.toString());
	}

	@Test
	public void indexValuesDescTest() {
		Target t = Target.indexValuesDesc("myIndex");
		assertEquals("indexvaluesdesc:myIndex", t.toString());
	}

	@Test
	public void nestedTest() {
		Query q = new Query();

		Query nested = new Query()
				.select("city")
				.select(sum(projection("salary")).as("salary"))
				.from("Employee")
				.groupBy(projection("city"))
				;

		Target t = Target.nested(nested);

		q.from(t)
		.where(projection("salary").gt(1000));
		assertEquals("SELECT FROM (SELECT city, sum(salary) as salary FROM Employee GROUP BY city) WHERE salary > 1000", q.toString());
	}

}