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

package it.raymanrt.orient.query;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProjectionMathOperatorsTest {

	@Test
	public void plusTest() {
		String simpleField = "clause";
		Projection p = Projection.projection(simpleField).plus(5);
		assertEquals("clause + 5", p.toString());
		assertEquals("clause", p.getName());
	}

	@Test
	public void minusTest() {
		String simpleField = "clause";
		Projection p = Projection.projection(simpleField).minus(5);
		assertEquals("clause - 5", p.toString());
		assertEquals("clause", p.getName());
	}

	@Test
	public void timesTest() {
		String simpleField = "clause";
		Projection p = Projection.projection(simpleField).times(5.0);
		assertEquals("clause * 5.0", p.toString());
		assertEquals("clause", p.getName());
	}

	@Test
	public void divideTest() {
		String simpleField = "clause";
		Projection p = Projection.projection(simpleField).divide(5.1);
		assertEquals("clause / 5.1", p.toString());
		assertEquals("clause", p.getName());
	}

	@Test
	public void modTest() {
		String simpleField = "clause";
		Projection p = Projection.projection(simpleField).mod(5);
		assertEquals("clause % 5", p.toString());
		assertEquals("clause", p.getName());
	}

}