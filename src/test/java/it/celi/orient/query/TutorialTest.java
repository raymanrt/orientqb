package it.celi.orient.query;

import org.junit.Test;

import static it.celi.orient.query.Clause.not;
import static it.celi.orient.query.Clause.or;
import static it.celi.orient.query.Projection.projection;
import static it.celi.orient.query.ProjectionFunction.max;
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

	@Test
	public void firstProjectionTest() {
		// static import is strongly suggested to improve your code readability
		// import static it.celi.orient.query.Projection.projection;
		// [...]

		Query q = new Query()
				.select(projection("field").as("f"))
				.from("Target");
		assertEquals("SELECT field as f FROM Target", q.toString());
	}

	@Test
	public void methodProjectionTest() {
		// static import is strongly suggested to improve your code readability
		// import static it.celi.orient.query.Projection.projection;
		// [...]

		Query q = new Query()
				.select(projection("field").normalize().as("fNormalized"))
				.from("Target");
		assertEquals("SELECT field.normalize() as fNormalized FROM Target", q.toString());
	}

	@Test
	public void functionProjectionTest() {
		// static import is strongly suggested to improve your code readability
		// import static it.celi.orient.query.Projection.projection;
		// [...]

		Query q = new Query()
				.select(max(projection("field")).as("fMax"))
				.from("Target");
		assertEquals("SELECT max(field) as fMax FROM Target", q.toString());
	}

	@Test
	public void targetTest() {
		Query q = new Query()
				.select("field")
				.from(Target.target("#1:1", "#1:2"));
		assertEquals("SELECT field FROM [#1:1, #1:2]", q.toString());
	}

	@Test
	public void whereTest() {
		Query q = new Query()
				.select(Projection.ALL)
				.from("Class")
				.where(projection("f2").eq(5));
		assertEquals("SELECT * FROM Class WHERE f2 = 5", q.toString());
	}

	@Test
	public void whereAndTest() {
		Query q = new Query()
				.select(Projection.ALL)
				.from("Class")
				.where(projection("f2").eq(5))
				.where(projection("f3").lt(0));
		assertEquals("SELECT * FROM Class WHERE f2 = 5 AND f3 < 0", q.toString());
	}

	@Test
	public void whereOrNotTest() {
		Query q = new Query()
				.select(Projection.ALL)
				.from("Class")
				.where(or(
						projection("f2").eq(5),
						not(projection("f3").lt(0))
				));
		assertEquals("SELECT * FROM Class WHERE f2 = 5 OR NOT(f3 < 0)", q.toString());
	}
}