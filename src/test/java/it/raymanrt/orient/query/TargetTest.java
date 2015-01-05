package it.raymanrt.orient.query;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TargetTest {

	@Test
	public void simpleTest() {
		Target t = Target.target("Class");
		assertEquals("Class", t.toString());
	}

	@Test
	public void multipleTest() {
		Target t = Target.target("#1:1", "#2:2");
		assertEquals("[#1:1, #2:2]", t.toString());
	}

	@Test
	public void clusterTest() {
		Target t = Target.cluster(5);
		assertEquals("cluster:5", t.toString());
	}

	@Test
	public void indexValuesTest() {
		Target t = Target.indexValues("myIndex");
		assertEquals("indexvalues:myIndex", t.toString());
	}

	@Test
	public void indexValuesAscTest() {
		Target t = Target.indexValuesAsc("myIndex");
		assertEquals("indexvaluesasc:myIndex", t.toString());
	}

	@Test
	public void indexValuesDescTest() {
		Target t = Target.indexValuesDesc("myIndex");
		assertEquals("indexvaluesdesc:myIndex", t.toString());
	}

}