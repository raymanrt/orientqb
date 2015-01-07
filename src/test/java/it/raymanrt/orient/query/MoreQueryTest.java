package it.raymanrt.orient.query;

import org.junit.Test;

import static it.raymanrt.orient.query.Projection.ALL;
import static it.raymanrt.orient.query.Projection.projection;
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
}