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

import static org.junit.Assert.assertEquals;

public class VariableTest {

	@Test
	public void variableTest() {
		Query q = new Query()
				.select(Variable.variable("var").as("alias"))
				.from("C");
		assertEquals("SELECT $var as alias FROM C", q.toString());
	}

	@Test
	public void thisTest() {
		Query q = new Query()
				.select(Variable.thisRecord().as("alias"))
				.from("C");
		assertEquals("SELECT @this as alias FROM C", q.toString());

		q = new Query()
				.select(Variable.thisRecord())
				.from("C");
		assertEquals("SELECT @this FROM C", q.toString());
	}

	@Test
	public void ridTest() {
		Query q = new Query()
				.select(Variable.rid().as("alias"))
				.from("C");
		assertEquals("SELECT @rid as alias FROM C", q.toString());
	}

	@Test
	public void classTest() {
		Query q = new Query()
				.select(Variable.classRecord().as("alias"))
				.from("C");
		assertEquals("SELECT @class as alias FROM C", q.toString());
	}

	@Test
	public void versionTest() {
		Query q = new Query()
				.select(Variable.version().as("alias"))
				.from("C");
		assertEquals("SELECT @version as alias FROM C", q.toString());
	}

	@Test
	public void sizeTest() {
		Query q = new Query()
				.select(Variable.size().as("alias"))
				.from("C");
		assertEquals("SELECT @size as alias FROM C", q.toString());
	}

	@Test
	public void typeTest() {
		Query q = new Query()
				.select(Variable.type().as("alias"))
				.from("C");
		assertEquals("SELECT @type as alias FROM C", q.toString());
	}

	@Test
	public void parentTest() {
		Query q = new Query()
				.select(Variable.parent().as("alias"))
				.from("C");
		assertEquals("SELECT $parent as alias FROM C", q.toString());
	}

	@Test
	public void currentTest() {
		Query q = new Query()
				.select(Variable.current().as("alias"))
				.from("C");
		assertEquals("SELECT $current as alias FROM C", q.toString());
	}

	@Test
	public void distanceTest() {
		Clause nearClause = ProjectionFunction.join(
				Projection.projection("myLat"), Projection.projection("myLon")
		).near(45.464161, 9.190336);
		Query q = new Query()
				.select(Variable.distance().as("alias"))
				.from("C")
				.where(nearClause);
		assertEquals("SELECT $distance as alias FROM C WHERE [myLat, myLon] NEAR [45.464161, 9.190336]", q.toString());
	}

}