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

public class VariableTest {

	@Test
	public void variableTest() {
		Query q = new Query()
				.select(variable("var").as("alias"))
				.from("C");
		assertEquals("SELECT $var as alias FROM C", q.toString());
	}

	@Test
	public void thisTest() {
		Query q = new Query()
				.select(thisRecord().as("alias"))
				.from("C");
		assertEquals("SELECT @this as alias FROM C", q.toString());

		q = new Query()
				.select(thisRecord())
				.from("C");
		assertEquals("SELECT @this FROM C", q.toString());
	}

	@Test
	public void ridTest() {
		Query q = new Query()
				.select(rid().as("alias"))
				.from("C");
		assertEquals("SELECT @rid as alias FROM C", q.toString());
	}

	@Test
	public void classTest() {
		Query q = new Query()
				.select(classRecord().as("alias"))
				.from("C");
		assertEquals("SELECT @class as alias FROM C", q.toString());
	}

	@Test
	public void versionTest() {
		Query q = new Query()
				.select(version().as("alias"))
				.from("C");
		assertEquals("SELECT @version as alias FROM C", q.toString());
	}

	@Test
	public void sizeTest() {
		Query q = new Query()
				.select(size().as("alias"))
				.from("C");
		assertEquals("SELECT @size as alias FROM C", q.toString());
	}

	@Test
	public void typeTest() {
		Query q = new Query()
				.select(type().as("alias"))
				.from("C");
		assertEquals("SELECT @type as alias FROM C", q.toString());
	}

	@Test
	public void parentTest() {
		Query q = new Query()
				.select(parent().as("alias"))
				.from("C");
		assertEquals("SELECT $parent as alias FROM C", q.toString());
	}

	@Test
	public void currentTest() {
		Query q = new Query()
				.select(current().as("alias"))
				.from("C");
		assertEquals("SELECT $current as alias FROM C", q.toString());
	}

	@Test
	public void distanceTest() {
		Clause nearClause = ProjectionFunction.join(
				projection("myLat"), projection("myLon")
		).near(45.464161, 9.190336);
		Query q = new Query()
				.select(distance().as("alias"))
				.from("C")
				.where(nearClause);
		assertEquals("SELECT $distance as alias FROM C WHERE [myLat, myLon] NEAR [45.464161, 9.190336]", q.toString());
	}

}