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

public class ParameterTest {

	@Test
	public void namedParameterTest() {
		Query q = new Query()
				.select(Projection.ALL)
				.from("C")
				.where(projection("field").lt(Parameter.parameter("x")))
				;
		assertEquals("SELECT * FROM C WHERE field < :x", q.toString());
	}

	@Test
	public void positionalParameterTest() {
		Query q = new Query()
				.select(Projection.ALL)
				.from("C")
				.where(projection("field").lt(Parameter.PARAMETER))
				;
		assertEquals("SELECT * FROM C WHERE field < ?", q.toString());
	}

}