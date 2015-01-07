package it.raymanrt.orient.query;

import org.junit.Test;

import java.util.regex.Pattern;

import static com.google.common.collect.Lists.newArrayList;
import static it.raymanrt.orient.query.Clause.clause;
import static it.raymanrt.orient.query.Projection.projection;
import static it.raymanrt.orient.query.Projection.value;
import static it.raymanrt.orient.query.Projection.list;
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
		Clause c = projection("x").containsText("txt");
		assertEquals("x CONTAINSTEXT 'txt'", c.toString());
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
}