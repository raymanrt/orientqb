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