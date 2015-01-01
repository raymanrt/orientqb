package it.celi.orient.query;

import org.junit.Test;

import static it.celi.orient.query.Parameter.PARAMETER;
import static it.celi.orient.query.Parameter.parameter;
import static it.celi.orient.query.Projection.projection;
import static org.junit.Assert.assertEquals;

public class ParameterTest {

	@Test
	public void namedParameterTest() {
		Query q = new Query()
				.select(Projection.ALL)
				.from("C")
				.where(projection("field").lt(parameter("x")))
				;
		assertEquals("SELECT * FROM C WHERE field < :x", q.toString());
	}

	@Test
	public void positionalParameterTest() {
		Query q = new Query()
				.select(Projection.ALL)
				.from("C")
				.where(projection("field").lt(PARAMETER))
				;
		assertEquals("SELECT * FROM C WHERE field < ?", q.toString());
	}

}