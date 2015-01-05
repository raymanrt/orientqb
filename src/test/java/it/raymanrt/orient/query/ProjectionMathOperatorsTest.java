package it.raymanrt.orient.query;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProjectionMathOperatorsTest {

	@Test
	public void plusTest() {
		String simpleField = "clause";
		Projection p = Projection.projection(simpleField).plus(5);
		assertEquals("clause + 5", p.toString());
		assertEquals("clause", p.getName());
	}

	@Test
	public void minusTest() {
		String simpleField = "clause";
		Projection p = Projection.projection(simpleField).minus(5);
		assertEquals("clause - 5", p.toString());
		assertEquals("clause", p.getName());
	}

	@Test
	public void timesTest() {
		String simpleField = "clause";
		Projection p = Projection.projection(simpleField).times(5.0);
		assertEquals("clause * 5.0", p.toString());
		assertEquals("clause", p.getName());
	}

	@Test
	public void divideTest() {
		String simpleField = "clause";
		Projection p = Projection.projection(simpleField).divide(5.1);
		assertEquals("clause / 5.1", p.toString());
		assertEquals("clause", p.getName());
	}

	@Test
	public void modTest() {
		String simpleField = "clause";
		Projection p = Projection.projection(simpleField).mod(5);
		assertEquals("clause % 5", p.toString());
		assertEquals("clause", p.getName());
	}

}