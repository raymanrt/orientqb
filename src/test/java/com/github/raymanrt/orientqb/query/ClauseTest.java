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

import com.github.raymanrt.orientqb.query.clause.AtomicClause;
import org.junit.Test;

import static com.github.raymanrt.orientqb.query.Clause.and;
import static com.github.raymanrt.orientqb.query.Clause.clause;
import static com.github.raymanrt.orientqb.query.Clause.not;
import static com.github.raymanrt.orientqb.query.Clause.or;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ClauseTest {

	@Test
	public void simpleWhereIntTest() {
		AtomicClause c = new AtomicClause("field", Operator.EQ, 5);
		assertEquals("field = 5", c.toString());

		c = new AtomicClause("field", Operator.NE, 5);
		assertEquals("field <> 5", c.toString());

		c = new AtomicClause("field", Operator.GT, 5);
		assertEquals("field > 5", c.toString());

		c = new AtomicClause("field", Operator.GE, 5);
		assertEquals("field >= 5", c.toString());

		c = new AtomicClause("field", Operator.LT, 5);
		assertEquals("field < 5", c.toString());

		c = new AtomicClause("field", Operator.LE, 5);
		assertEquals("field <= 5", c.toString());
	}
	@Test
	public void simpleFactoryWhereIntTest() {
		Clause c = clause("field", Operator.EQ, 5);
		assertEquals("field = 5", c.toString());

		c = clause("field", Operator.NE, 5);
		assertEquals("field <> 5", c.toString());

		c = clause("field", Operator.GT, 5);
		assertEquals("field > 5", c.toString());

		c = clause("field", Operator.GE, 5);
		assertEquals("field >= 5", c.toString());

		c = clause("field", Operator.LT, 5);
		assertEquals("field < 5", c.toString());

		c = clause("field", Operator.LE, 5);
		assertEquals("field <= 5", c.toString());
	}

	@Test
	 public void simpleWhereDecimalTest() {
		AtomicClause c = new AtomicClause("field", Operator.EQ, 5.0);
		assertEquals("field = 5.0", c.toString());

		c = new AtomicClause("field", Operator.NE, 5.0);
		assertEquals("field <> 5.0", c.toString());

		c = new AtomicClause("field", Operator.GT, 5.0);
		assertEquals("field > 5.0", c.toString());

		c = new AtomicClause("field", Operator.GE, 5.0);
		assertEquals("field >= 5.0", c.toString());

		c = new AtomicClause("field", Operator.LT, 5.0);
		assertEquals("field < 5.0", c.toString());

		c = new AtomicClause("field", Operator.LE, 5.0);
		assertEquals("field <= 5.0", c.toString());
	}

	@Test
	public void simpleWhereStringTest() {
		AtomicClause c = new AtomicClause("field", Operator.EQ, "hello world");
		assertEquals("field = 'hello world'", c.toString());

		c = new AtomicClause("field", Operator.NE, "hello world");
		assertEquals("field <> 'hello world'", c.toString());

		c = new AtomicClause("field", Operator.LIKE, "%hello world");
		assertEquals("field LIKE '%hello world'", c.toString());
	}

	@Test
	public void andWhereTest() {
		Clause c = and(
				clause("f1", Operator.EQ, 5),
				clause("f2", Operator.NE, "hello")
		);

		assertEquals("f1 = 5 AND f2 <> 'hello'", c.toString());
	}

	@Test
	public void orWhereTest() {
		Clause c = or(
				clause("f1", Operator.EQ, 5),
				clause("f2", Operator.NE, "hello"),
				clause("f2", Operator.GT, 1.0)
		);

		assertEquals("f1 = 5 OR f2 <> 'hello' OR f2 > 1.0", c.toString());
	}

	@Test
	public void nestedWhereTest() {
		Clause c = or(
				clause("f1", Operator.EQ, 5),
				and(
						clause("f2", Operator.NE, "hello"),
						clause("f2", Operator.GT, 1.0)
				)
		);

		assertEquals("f1 = 5 OR ( f2 <> 'hello' AND f2 > 1.0 )", c.toString());
	}

	@Test
	public void twoNestedWhereTest() {
		Clause c = or(
				or(
						clause("f1", Operator.GT, 10),
						clause("f1", Operator.LE, 100)
				),
				and(
					clause("f2", Operator.NE, "hello"),
					clause("f2", Operator.GT, 1.0)
				)
		);

		assertEquals("( f1 > 10 OR f1 <= 100 ) OR ( f2 <> 'hello' AND f2 > 1.0 )", c.toString());
	}

	@Test
	public void projectionWhereTest() {
		Projection p = ProjectionFunction.out("prj").size().as("p");
		Clause c = clause(p, Operator.EQ, 5);


		assertEquals("out('prj').size() = 5", c.toString());
	}

	@Test
	public void fieldTest() {
		Projection p = ProjectionFunction.out("prj");
		Clause c = p.field("test", 5);
		assertEquals("out('prj')[test = 5]", c.toString());

		c = p.field("test", "test'd");
		assertEquals("out('prj')[test = 'test\\'d']", c.toString());
	}

    @Test
    public void notTest() {
        Clause c = not(clause("x", Operator.EQ, 5));
        assertEquals("NOT x = 5", c.toString());

        c = not(clause("x", Operator.EQ, "5"));
        assertEquals("NOT x = '5'", c.toString());
    }

    @Test
    public void notWithManyClausesTest() {
        Clause c = not(and(
                clause("x", Operator.EQ, 5),
                clause("y", Operator.LE, 5)
        ));
        assertEquals("NOT (x = 5 AND y <= 5)", c.toString());
    }

    @Test
    public void isEmptyTest() {
        Clause c = and();
        assertTrue(c.isEmpty());

        Projection p = ProjectionFunction.out("prj");
        c = p.field("test", 5);
        assertFalse(c.isEmpty());

        Projection q = ProjectionFunction.out("prj");
        Clause c1 = c;
        Clause c2 = q.field("test", "hello");
        c = and(c1, c2);
        assertFalse(c.isEmpty());
    }

    @Test
    public void emptyClauseQueryTest() {
        Clause empty = and();
        assertTrue(empty.isEmpty());

        Clause notEmpty = Projection.projection("x").field("test", 5);

        Query q1 = new Query()
                .from("SomeClass")
                .where(notEmpty);

        Query q2 = new Query()
                .from("SomeClass")
                .where(empty)
                .where(notEmpty)
                .where(empty);

        assertEquals(q1.toString(), q2.toString());
    }


}