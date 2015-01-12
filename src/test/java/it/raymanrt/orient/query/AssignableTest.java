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

import static it.raymanrt.orient.query.Projection.projection;
import static org.junit.Assert.assertEquals;

public class AssignableTest {

	@Test
	public void atomicProjectionTest() {
		Assignable a = projection("field").as("alias");
		assertEquals("field", a.getAssignment());
	}

	@Test
	public void compositeProjectionTest() {
		Assignable a = ProjectionFunction.out()
				.dot(projection("field").as("alias1"))
				.as("alias2");
		assertEquals("out().field", a.getAssignment());
		assertEquals("out().field as alias2", a.toString());
	}

}