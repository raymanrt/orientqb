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

import static com.github.raymanrt.orientqb.query.ProjectionFunction.distinct;
import static com.github.raymanrt.orientqb.query.ProjectionFunction.set;
import static com.github.raymanrt.orientqb.query.ProjectionFunction.union;
import static java.lang.String.format;
import static org.junit.Assert.assertEquals;

public class ProjectionTest {

	@Test
	public void simpleProjectionTest() {
		String simpleField = "clause";
		Projection p = Projection.projection(simpleField);
		assertEquals(simpleField, p.toString());
		assertEquals(simpleField, p.getName());
	}

	@Test
	public void projectionWithAliasTest() {
		String field = "f";
		String alias = "a";
		Projection p = Projection.projection(field).as(alias);
		assertEquals(format("%s as %s", field, alias), p.toString());
		assertEquals(alias, p.getName());
	}

	@Test
	public void projectionWithModifierTest() {
		String field = "f";
		String alias = "a";
		Projection p = union(Projection.projection(field)).as(alias);
		assertEquals(format("union(%s) as %s", field, alias), p.toString());
		assertEquals(alias, p.getName());
	}

	@Test
	public void newProjectionModifiersTest() {
		String field = "f";
		String alias = "a";

		Projection p = distinct(
				set(
						union(
								Projection.projection(field)
						)
				).index(0)
		).as(alias);

		assertEquals(format("distinct(set(union(%s))[0]) as %s", field, alias), p.toString());
	}

    @Test
    public void projectionLikeTest() {
        Clause c = Projection.projection("field").like(Parameter.parameter("var"));
        assertEquals("field LIKE :var", c.toString());
    }

}