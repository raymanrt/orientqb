package it.celi.orient.query;

import org.junit.Test;

import static it.celi.orient.query.Projection.ALL;
import static it.celi.orient.query.Projection.projection;
import static org.junit.Assert.assertEquals;

public class TutorialTest {

	@Test
	public void firstQueryTest() {
		Query q = new Query();
		assertEquals("SELECT FROM V", q.toString());
	}

	@Test
	public void projectionQueryTest() {
		Query q = new Query()
				.select("hello")
				.select("world");
		assertEquals("SELECT hello, world FROM V", q.toString());
	}

	@Test
	public void targetQueryTest() {
		Query q = new Query()
				.select("field")
				.from("Target");
		assertEquals("SELECT field FROM Target", q.toString());
	}
}