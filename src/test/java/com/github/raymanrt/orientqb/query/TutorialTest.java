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

import static com.github.raymanrt.orientqb.query.Clause.not;
import static com.github.raymanrt.orientqb.query.Clause.or;
import static com.github.raymanrt.orientqb.query.Projection.projection;
import static com.github.raymanrt.orientqb.query.ProjectionFunction.max;
import static org.junit.Assert.assertEquals;

public class TutorialTest {

	@Test
	public void firstQueryTest() {
		Query q = new Query();
		assertEquals("SELECT FROM V", q.toString());
	}

	@Test
	public void projectionQueryTest() {
		Query q = new Query()
				.select("hello")
				.select("world");
		assertEquals("SELECT hello, world FROM V", q.toString());
	}

	@Test
	public void targetQueryTest() {
		Query q = new Query()
				.select("field")
				.from("Target");
		assertEquals("SELECT field FROM Target", q.toString());
	}

	@Test
	public void firstProjectionTest() {
		// static import is strongly suggested to improve your code readability
		// import static Projection.projection;
		// [...]

		Query q = new Query()
				.select(projection("field").as("f"))
				.from("Target");
		assertEquals("SELECT field as f FROM Target", q.toString());
	}

	@Test
	public void methodProjectionTest() {
		// static import is strongly suggested to improve your code readability
		// import static Projection.projection;
		// [...]

		Query q = new Query()
				.select(projection("field").normalize().as("fNormalized"))
				.from("Target");
		assertEquals("SELECT field.normalize() as fNormalized FROM Target", q.toString());
	}

	@Test
	public void functionProjectionTest() {
		// static import is strongly suggested to improve your code readability
		// import static Projection.projection;
		// [...]

		Query q = new Query()
				.select(max(projection("field")).as("fMax"))
				.from("Target");
		assertEquals("SELECT max(field) as fMax FROM Target", q.toString());
	}

	@Test
	public void dotIndexTest() {
		Query q = new Query()
				.select(
						projection("field")
								.dot(projection("subField"))
								.index(0)
				)
				.from("Class");
		assertEquals("SELECT field.subField[0] FROM Class", q.toString());
	}

	@Test
	public void mathTest() {
		Query q = new Query()
				.select(
					projection("field").times(2).plus(1)
				)
				.from("Class");
		assertEquals("SELECT field * 2 + 1 FROM Class", q.toString());
	}

	@Test
	public void targetTest() {
		Query q = new Query()
				.select("field")
				.from(Target.target("#1:1", "#1:2"));
		assertEquals("SELECT field FROM [#1:1, #1:2]", q.toString());
	}

	@Test
	public void whereTest() {
		Query q = new Query()
				.select(Projection.ALL)
				.from("Class")
				.where(projection("f2").eq(5));
		assertEquals("SELECT * FROM Class WHERE f2 = 5", q.toString());
	}

	@Test
	public void whereAndTest() {
		Query q = new Query()
				.select(Projection.ALL)
				.from("Class")
				.where(projection("f2").eq(5))
				.where(projection("f3").lt(0));
		assertEquals("SELECT * FROM Class WHERE f2 = 5 AND f3 < 0", q.toString());
	}

	@Test
	public void whereOrNotTest() {
		Query q = new Query()
				.select(Projection.ALL)
				.from("Class")
				.where(or(
						projection("f2").eq(5),
						not(projection("f3").lt(0))
				));
		assertEquals("SELECT * FROM Class WHERE f2 = 5 OR NOT(f3 < 0)", q.toString());
	}

	@Test
	public void parameterTest() {
		Query q = new Query()
				.select(Projection.ALL)
				.from("Class")
				.where(projection("f").eq(Parameter.PARAMETER));
		assertEquals("SELECT * FROM Class WHERE f = ?", q.toString());
	}

	@Test
	public void namedParameterTest() {
		Query q = new Query()
				.select(Projection.ALL)
				.from("Class")
				.where(projection("f").eq(Parameter.parameter("fParameter")));
		assertEquals("SELECT * FROM Class WHERE f = :fParameter", q.toString());
	}
}