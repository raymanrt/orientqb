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

import java.util.regex.Pattern;

import static com.github.raymanrt.orientqb.query.Clause.clause;
import static com.github.raymanrt.orientqb.query.Parameter.parameter;
import static com.github.raymanrt.orientqb.query.Projection.list;
import static com.github.raymanrt.orientqb.query.Projection.projection;
import static com.github.raymanrt.orientqb.query.Projection.value;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;

public class OperatorTest {

	@Test
	public void eqTest() {
		Clause c = clause("x", Operator.EQ, 5);
		assertEquals("x = 5", c.toString());

		c = projection("x").eq(5);
		assertEquals("x = 5", c.toString());

		c = clause("x", Operator.EQ, "5");
		assertEquals("x = '5'", c.toString());
	}

	@Test
	public void neTest() {
		Clause c = clause("x", Operator.NE, 5);
		assertEquals("x <> 5", c.toString());

		c = projection("x").ne(5);
		assertEquals("x <> 5", c.toString());

		c = clause("x", Operator.NE, "5");
		assertEquals("x <> '5'", c.toString());
	}

	@Test
	public void ltTest() {
		Clause c = clause("x", Operator.LT, 5);
		assertEquals("x < 5", c.toString());

		c = projection("x").lt(5);
		assertEquals("x < 5", c.toString());

		c = clause("x", Operator.LT, "5");
		assertEquals("x < '5'", c.toString());
	}

	@Test
	public void leTest() {
		Clause c = clause("x", Operator.LE, 5);
		assertEquals("x <= 5", c.toString());

		c = projection("x").le(5);
		assertEquals("x <= 5", c.toString());

		c = clause("x", Operator.LE, "5");
		assertEquals("x <= '5'", c.toString());
	}

	@Test
	public void gtTest() {
		Clause c = clause("x", Operator.GT, 5);
		assertEquals("x > 5", c.toString());

		c = projection("x").gt(5);
		assertEquals("x > 5", c.toString());

		c = clause("x", Operator.GT, "5");
		assertEquals("x > '5'", c.toString());
	}

	@Test
	public void geTest() {
		Clause c = clause("x", Operator.GE, 5);
		assertEquals("x >= 5", c.toString());

		c = projection("x").ge(5);
		assertEquals("x >= 5", c.toString());

		c = clause("x", Operator.GE, "5");
		assertEquals("x >= '5'", c.toString());
	}

	@Test
	public void instanceOfTest() {
		Clause c = clause("x", Operator.INSTANCEOF, "string");
		assertEquals("x INSTANCEOF 'string'", c.toString());

        c = projection("x").instanceOf(DataType.BOOLEAN);
        assertEquals("x INSTANCEOF 'BOOLEAN'", c.toString());

        c = Variable.thisRecord().instanceOf("MyClass");
        assertEquals("@this INSTANCEOF 'MyClass'", c.toString());
	}

	@Test
	public void inTest() {
		Clause c = clause("x", Operator.IN, "string");
		assertEquals("x IN 'string'", c.toString());
		c = projection("x").in(value("string"));
		assertEquals("x IN 'string'", c.toString());

		c = clause("x", Operator.IN, newArrayList("a", "b", "c"));
		assertEquals("x IN ['a', 'b', 'c']", c.toString());
		c = projection("x").in(list("a", "b", "c"));
		assertEquals("x IN ['a', 'b', 'c']", c.toString());

		c = clause("x", Operator.IN, newArrayList("a", 5, "c"));
		assertEquals("x IN ['a', 5, 'c']", c.toString());
		c = projection("x").in(list("a", 5, "c"));
		assertEquals("x IN ['a', 5, 'c']", c.toString());

		c = clause("x", Operator.IN, newArrayList(1));
		assertEquals("x IN [1]", c.toString());
		c = projection("x").in(list(1));
		assertEquals("x IN [1]", c.toString());

		c = clause("x", Operator.IN, ProjectionFunction.out("friends"));
		assertEquals("x IN out('friends')", c.toString());
		c = projection("x").in(ProjectionFunction.out("friends"));
		assertEquals("x IN out('friends')", c.toString());
	}

	@Test
	public void notInTest() {
		Clause c = clause("x", Operator.NOT_IN, "string");
		assertEquals("x NOT IN 'string'", c.toString());
		c = projection("x").notIn(value("string"));
		assertEquals("x NOT IN 'string'", c.toString());

		c = clause("x", Operator.NOT_IN, newArrayList("a", "b", "c"));
		assertEquals("x NOT IN ['a', 'b', 'c']", c.toString());
		c = projection("x").notIn(list("a", "b", "c"));
		assertEquals("x NOT IN ['a', 'b', 'c']", c.toString());

		c = clause("x", Operator.NOT_IN, newArrayList("a", 5, "c"));
		assertEquals("x NOT IN ['a', 5, 'c']", c.toString());
		c = projection("x").notIn(list("a", 5, "c"));
		assertEquals("x NOT IN ['a', 5, 'c']", c.toString());

		c = clause("x", Operator.NOT_IN, newArrayList(1));
		assertEquals("x NOT IN [1]", c.toString());
		c = projection("x").notIn(list(1));
		assertEquals("x NOT IN [1]", c.toString());

		c = clause("x", Operator.NOT_IN, ProjectionFunction.out("friends"));
		assertEquals("x NOT IN out('friends')", c.toString());
		c = projection("x").notIn(ProjectionFunction.out("friends"));
		assertEquals("x NOT IN out('friends')", c.toString());

		// #12: select from T where attr1="toto" AND attr2 NOT IN ['val1', 'val2']
		c = clause("attr2", Operator.BETWEEN.NOT_IN, newArrayList("val1", "val2"));
		assertEquals("attr2 NOT IN ['val1', 'val2']", c.toString());
		c = projection("attr2").notIn(list("val1", "val2"));
		assertEquals("attr2 NOT IN ['val1', 'val2']", c.toString());
	}

	@Test
	public void containsTest() {
		Clause c = projection("x").contains(value("name"));
		assertEquals("x CONTAINS 'name'", c.toString());

		c = projection("x").contains(Variable.rid());
		assertEquals("x CONTAINS @rid", c.toString());
	}

	@Test
	public void containsKeyTest() {
		Clause c = projection("x").containsKey("name");
		assertEquals("x CONTAINSKEY 'name'", c.toString());
	}

	@Test
	public void containsValueTest() {
		Clause c = projection("x").containsValue("name");
		assertEquals("x CONTAINSVALUE 'name'", c.toString());

		c = projection("x").containsValue(5.1);
		assertEquals("x CONTAINSVALUE 5.1", c.toString());
	}

	@Test
	public void containsTextTest() {
		Clause c = projection("x").containsText(value("txt"));
		assertEquals("x CONTAINSTEXT 'txt'", c.toString());

		c = projection("x").containsText(parameter("txt"));
		assertEquals("x CONTAINSTEXT :txt", c.toString());
	}

	@Test
	public void matchesTest() {
		Clause c = projection("x").matches(Pattern.compile("[w]+"));
		assertEquals("x MATCHES '[w]+'", c.toString());
	}

	@Test
	public void luceneTest() {
		Clause c = projection("x").lucene("test*");
		assertEquals("x LUCENE 'test*'", c.toString());
	}

	@Test
	public void nearTest() {
		Clause c = ProjectionFunction.join(
				projection("myLat"), projection("myLon")
		).near(45.464161, 9.190336);
		assertEquals("[myLat, myLon] NEAR [45.464161, 9.190336]", c.toString());

		c = ProjectionFunction.join(
				projection("myLat"), projection("myLon")
		).near(45.464161, 9.190336, 200);
		assertEquals("[myLat, myLon] NEAR [45.464161, 9.190336, {'maxDistance': 200}]", c.toString());

		c = ProjectionFunction.join(
				projection("myLat"), projection("myLon")
		).near(45.464161, 9.190336, ProjectionFunction.out("xxx"));
		assertEquals("[myLat, myLon] NEAR [45.464161, 9.190336, {'maxDistance': out('xxx')}]", c.toString());
	}

	@Test
	public void withinTest() {
		Clause c = ProjectionFunction.join(
				projection("myLat"), projection("myLon")
		).within(
				new Number[]{45.464161, 9.190336},
				new Number[]{46.066667, 11.116667},
				new Number[]{45.066667, 7.7}

				);
		assertEquals("[myLat, myLon] WITHIN [[45.464161, 9.190336], [46.066667, 11.116667], [45.066667, 7.7]]", c.toString());
	}

	@Test
	public void betweenTest() {
		Clause c = projection("x").between(5, 6.5);
		assertEquals("x BETWEEN 5 AND 6.5", c.toString());
	}

    @Test
    public void definedTest() {
        Clause c = projection("x").defined();
        assertEquals("x IS DEFINED", c.toString());
    }

    @Test
    public void notDefinedTest() {
        Clause c = projection("x").notDefined();
        assertEquals("x IS NOT DEFINED", c.toString());
    }

    @Test
    public void isNullTest() {
        Clause c = projection("x").isNull();
        assertEquals("x IS NULL", c.toString());
    }

    @Test
    public void notNullTest() {
        Clause c = projection("x").notNull();
        assertEquals("x IS NOT NULL", c.toString());
    }
}