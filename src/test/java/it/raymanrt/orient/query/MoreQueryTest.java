package it.raymanrt.orient.query;

import org.junit.Test;

import static it.raymanrt.orient.query.Parameter.PARAMETER;
import static it.raymanrt.orient.query.Parameter.parameter;
import static it.raymanrt.orient.query.Projection.projection;
import static it.raymanrt.orient.query.ProjectionFunction.out;
import static org.junit.Assert.assertEquals;

public class MoreQueryTest {

	@Test
	public void complexWhereQueryTest() {
		Query q = new Query()
			.where(projection("a").eq(true))
			.where(Clause.or(
					projection("b").eq(true),
					projection("c").eq(true)
			))
		;
		assertEquals("SELECT FROM V WHERE a = true AND (b = true OR c = true)", q.toString());
	}

	@Test
	public void notVeryComplexWhereQueryTest() {
		Query q = new Query()
				.where(Clause.or(
						projection("b").eq(true),
						projection("c").eq(true)
				))
				;
		assertEquals("SELECT FROM V WHERE b = true OR c = true", q.toString());
	}

	@Test
	public void parameterAsProjectionTest() {
		Query q = new Query()
				.where(PARAMETER.eq(5));
		assertEquals("SELECT FROM V WHERE ? = 5", q.toString());
	}

	@Test
	public void namedParameterAsProjectionTest() {
		Query q = new Query()
				.where(out("label").contains(parameter("par")));
		assertEquals("SELECT FROM V WHERE out('label') CONTAINS :par", q.toString());
	}
}