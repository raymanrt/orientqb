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