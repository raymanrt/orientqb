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

import static com.github.raymanrt.orientqb.query.Projection.ALL;
import static com.github.raymanrt.orientqb.query.Projection.projection;
import static org.junit.Assert.assertEquals;

public class QueryTest {

	@Test
	public void emptyQueryTest() {
		Query q = new Query();
		assertEquals("SELECT FROM V", q.toString());
	}
	
	@Test
	public void allQueryTest() {
		Query q = new Query();
		q.select(Projection.ALL);
		q.from("Class");
		assertEquals("SELECT * FROM Class", q.toString());
	}
	
	@Test
	public void oneFieldQueryTest() {
		Query q = new Query();
		q.select("f1");
		q.from("Class");
		assertEquals("SELECT f1 FROM Class", q.toString());
	}
	
	@Test
	public void twoFieldsQueryTest() {
		Query q = new Query();
		q.select("f1");
		q.select("f2");
		q.from("Class");
		assertEquals("SELECT f1, f2 FROM Class", q.toString());
	}
	
	@Test
	public void twoFieldsInOneQueryTest() {
		Query q = new Query();
		q.select("f1", "f2");
		q.from("Class");
		assertEquals("SELECT f1, f2 FROM Class", q.toString());
	}
	
	@Test
	public void oneFieldWithAliasTest() {
		Query q = new Query();
		q.select(projection("f1").as("field1"));
		q.from("Class");
		assertEquals("SELECT f1 as field1 FROM Class", q.toString());
	}
	
	@Test
	public void twoFieldsWithAliasQueryTest() {
		Query q = new Query();
		q.select(projection("f1").as("field1"));
		q.select(projection("f2").as("field2"));
		q.from("Class");
		assertEquals("SELECT f1 as field1, f2 as field2 FROM Class", q.toString());
	}
	
	@Test
	public void twoFieldsInOneWithAliasQueryTest() {
		Query q = new Query();
		q.select(projection("f1").as("field1"), projection("f2").as("field2"));
		q.from("Class");
		assertEquals("SELECT f1 as field1, f2 as field2 FROM Class", q.toString());
	}
	
	@Test
	public void mixedFieldsQueryTest() {
		Query q = new Query();
		q.select(projection("f1").as("field1"));
		q.select("f2");
		q.from("Class");
		assertEquals("SELECT f1 as field1, f2 FROM Class", q.toString());
	}
	
	@Test
	public void queryWithAliasTest() {
		Query q = new Query();
		q.select(Projection.ALL);
		q.from("Class");
		assertEquals("SELECT * FROM Class", q.toString());
	}

	@Test
	public void letQueryTest() {
		Query q = new Query();
		Query q1 = new Query();
		q.let("x", q1);
		
		assertEquals("SELECT FROM V LET $x = ( SELECT FROM V )", q.toString());
	}

	@Test
	public void letOverwriteQueryTest() {
		Query q = new Query();
		Query q1 = new Query().from("V");
		Query q2 = new Query().from("C");
		q.let("x", q1);
		q.let("x", q2);
		
		assertEquals("SELECT FROM V LET $x = ( SELECT FROM C )", q.toString());
	}

	@Test
	public void letTwoVariablesQueryTest() {
		Query q = new Query();
		Query q1 = new Query().from("V");
		Query q2 = new Query().from("C");
		q.let("x", q1);
		q.let("y", q2);
		
		assertEquals("SELECT FROM V LET $x = ( SELECT FROM V ), $y = ( SELECT FROM C )", q.toString());
	}

	@Test
	public void letTwoVariablesMixedQueryTest() {
		Query q = new Query();
		q.let("x", new Query().from("V"));
		Query yQuery = new Query()
			.select("")
			.from("C");
		q.let("y", yQuery);
		
		assertEquals("SELECT FROM V LET $x = ( SELECT FROM V ), $y = ( SELECT FROM C )", q.toString());
	}

	@Test
	public void fetchplanQueryTest() {
		Query q = new Query()
				.select("field")
				.from("V")
				.fetchPlan(new FetchingStrategy(Level.anyLevel(), ALL, FetchingStrategy.UNLIMITED));
		assertEquals("SELECT field FROM V FETCHPLAN [*]*:-1", q.toString());

		q = new Query()
				.select("field")
				.from("V")
				.fetchPlan(new FetchingStrategy(ALL, FetchingStrategy.UNLIMITED))
                .fetchPlan(new FetchingStrategy(Level.level(1), "field", FetchingStrategy.EXCLUDE_CURRENT));
		assertEquals("SELECT field FROM V FETCHPLAN *:-1 [1]field:-2", q.toString());

		q = new Query()
				.select("field")
				.from("V")
				.fetchPlan(
						new FetchingStrategy(ALL, FetchingStrategy.UNLIMITED),
						new FetchingStrategy("field", FetchingStrategy.EXCLUDE_CURRENT),
						new FetchingStrategy("field", FetchingStrategy.EXCLUDE_CURRENT)
				);
		assertEquals("SELECT field FROM V FETCHPLAN *:-1 field:-2", q.toString());

		q = new Query()
				.select("field")
				.from("V")
				.fetchPlan(new FetchingStrategy(Level.level(-5), projection("field").dot(projection("name")), 10));
		assertEquals("SELECT field FROM V FETCHPLAN [-5]field.name:10", q.toString());
	}

	@Test
	public void timeoutQueryTest() {
		Query q = new Query()
				.select("field")
				.from("V")
				.timeout(100, TimeoutStrategy.RETURN);
		assertEquals("SELECT field FROM V TIMEOUT 100 RETURN", q.toString());

		q = new Query()
				.select("field")
				.from("V")
				.timeout(100);
		assertEquals("SELECT field FROM V TIMEOUT 100", q.toString());
	}

	@Test
	public void lockQueryTest() {
		Query q = new Query()
				.select("field")
				.from("V")
				.lock(LockingStrategy.RECORD);
		assertEquals("SELECT field FROM V LOCK RECORD", q.toString());

		q.lockReset();
		assertEquals("SELECT field FROM V", q.toString());

		q.lockRecord();
		assertEquals("SELECT field FROM V LOCK RECORD", q.toString());

		q.lock(LockingStrategy.DEFAULT);
		assertEquals("SELECT field FROM V LOCK DEFAULT", q.toString());

	}

	@Test
	public void parallelQueryTest() {
		Query q = new Query()
				.select("field")
				.from("V")
				.parallel();
		assertEquals("SELECT field FROM V PARALLEL", q.toString());

		q.parallel();
		assertEquals("SELECT field FROM V PARALLEL", q.toString());

		q.parallel(false);
		assertEquals("SELECT field FROM V", q.toString());

		q.parallel(false).parallel().parallel(false).parallel(true);
		assertEquals("SELECT field FROM V PARALLEL", q.toString());
	}

	@Test
	public void outQueryTest() {
		Query q = new Query()
				.select(ProjectionFunction.out("hasPermission"))
				.from("V");

		assertEquals("SELECT out('hasPermission') FROM V", q.toString());
	}

}
