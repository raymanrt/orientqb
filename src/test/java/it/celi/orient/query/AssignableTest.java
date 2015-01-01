package it.celi.orient.query;

import org.junit.Test;

import static it.celi.orient.query.Projection.projection;
import static it.celi.orient.query.Variable.classRecord;
import static it.celi.orient.query.Variable.current;
import static it.celi.orient.query.Variable.distance;
import static it.celi.orient.query.Variable.parent;
import static it.celi.orient.query.Variable.rid;
import static it.celi.orient.query.Variable.size;
import static it.celi.orient.query.Variable.thisRecord;
import static it.celi.orient.query.Variable.type;
import static it.celi.orient.query.Variable.variable;
import static it.celi.orient.query.Variable.version;
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