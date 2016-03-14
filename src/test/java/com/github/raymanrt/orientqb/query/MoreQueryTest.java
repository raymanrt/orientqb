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

import static com.github.raymanrt.orientqb.query.Parameter.PARAMETER;
import static com.github.raymanrt.orientqb.query.Parameter.parameter;
import static com.github.raymanrt.orientqb.query.Projection.projection;
import static com.github.raymanrt.orientqb.query.ProjectionFunction.expand;
import static com.github.raymanrt.orientqb.query.ProjectionFunction.out;
import static com.github.raymanrt.orientqb.query.ProjectionFunction.unionAll;
import static com.github.raymanrt.orientqb.query.Target.nested;
import static com.github.raymanrt.orientqb.query.Variable.variable;
import static org.junit.Assert.assertEquals;

public class MoreQueryTest {

	@Test
	public void complexWhereQueryTest() {
		Query q = new Query()
			.where(projection("a").eq(true))
			.where(Clause.or(
					projection("b").eq(true),
					projection("c").eq(true)
			))
		;
		assertEquals("SELECT FROM V WHERE a = true AND ( b = true OR c = true )", q.toString());
	}

	@Test
	public void notVeryComplexWhereQueryTest() {
		Query q = new Query()
				.where(Clause.or(
						projection("b").eq(true),
						projection("c").eq(true)
				))
				;
		assertEquals("SELECT FROM V WHERE b = true OR c = true", q.toString());
	}

	@Test
	public void parameterAsProjectionTest() {
		Query q = new Query()
				.where(PARAMETER.eq(5));
		assertEquals("SELECT FROM V WHERE ? = 5", q.toString());
	}

	@Test
	public void namedParameterAsProjectionTest() {
		Query q = new Query()
				.where(out("label").contains(parameter("par")));
		assertEquals("SELECT FROM V WHERE out('label') CONTAINS :par", q.toString());
	}

	@Test
	public void nestedUnionAllQueryTest() {
		Query q = new Query()
				.from(nested(new Query()
						.select(expand(variable("c")))
						.let("a", new Query().from("D"))
						.let("b", new Query().from("I"))
						.let("c", unionAll(variable("a"), variable("b")))
						.fromEmpty()
				))
				.where(projection("value").eq(5))
				.limit(10);

		assertEquals("SELECT FROM (SELECT expand($c) " +
				"LET $a = ( SELECT FROM D ), $b = ( SELECT FROM I ), $c = unionAll($a, $b)) " +
				"WHERE value = 5 " +
				"LIMIT 10", q.toString());

	}
}