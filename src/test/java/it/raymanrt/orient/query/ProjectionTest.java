package it.raymanrt.orient.query;

import org.junit.Test;

import static it.raymanrt.orient.query.ProjectionFunction.distinct;
import static it.raymanrt.orient.query.ProjectionFunction.set;
import static it.raymanrt.orient.query.ProjectionFunction.union;
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

}