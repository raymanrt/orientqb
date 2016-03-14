/*
 * Copyright 2015 Riccardo Tasso
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.raymanrt.orientqb.query;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static com.github.raymanrt.orientqb.query.Projection.projection;
import static com.github.raymanrt.orientqb.query.Projection.value;
import static com.github.raymanrt.orientqb.query.ProjectionFunction.*;
import static org.junit.Assert.assertEquals;

public class ProjectionFunctionTest {

	@Test
	public void columnProjectionTest() {
		int index = 2;

		Projection p = column(index);
		assertEquals("column(2)", p.toString());

		p.as("col");
		assertEquals("column(2) as col", p.toString());
	}

	@Test
	public void avgTest() {
		String field = "field";

		Projection p = avg(projection(field));
		assertEquals("avg(field)", p.toString());

		p.as("avg_field");
		assertEquals("avg(field) as avg_field", p.toString());

		p = avg(projection(field), projection("anotherField"));
		assertEquals("avg(field, anotherField)", p.toString());
	}

	@Test
	public void bothTest() {
		String label1 = "label1";
		String label2 = "label2";

		Projection p = both(label1);
		assertEquals("both('label1')", p.toString());

		p = both(label1, label2);
		assertEquals("both('label1', 'label2')", p.toString());
	}

	@Test
	public void bothETest() {
		String label1 = "label1";
		String label2 = "label2";

		Projection p = bothE(label1);
		assertEquals("bothE('label1')", p.toString());

		p = bothE(label1, label2);
		assertEquals("bothE('label1', 'label2')", p.toString());
	}

	@Test
	public void coalesceTest() {
		String label1 = "l1";
		String label2 = "l2";

		Projection p = coalesce(projection(label1));
		assertEquals("coalesce(l1)", p.toString());

		p = coalesce(projection(label1), projection(label2));
		assertEquals("coalesce(l1, l2)", p.toString());

		p = coalesce(projection(label1), projection(label2).size()).as("c");
		assertEquals("coalesce(l1, l2.size()) as c", p.toString());
	}

	@Test
	public void countTest() {
		String field = "f1";
		String label = "f2";

		Projection p = count(projection(field));
		assertEquals("count(f1)", p.toString());

		p.as(label);
		assertEquals("count(f1) as f2", p.toString());
	}

	@Test
	public void dateTest() {

		Projection p = date(value("22-03-1983"));
		assertEquals("date('22-03-1983')", p.toString());

		p = date(projection("birthday").dot(projection("date")));
		assertEquals("date(birthday.date)", p.toString());

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/YYYY");
		p = date(projection("birthday").dot(projection("date")), format);
		assertEquals("date(birthday.date, 'dd/MM/YYYY')", p.toString());

		format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
		TimeZone timezone = TimeZone.getTimeZone("America/Los_Angeles");
		p = date(projection("birthday").dot(projection("date")), format, timezone);
		assertEquals("date(birthday.date, 'EEE, d MMM yyyy HH:mm:ss Z', 'America/Los_Angeles')", p.toString());
	}

	@Test
	public void differenceTest() {
		String label1 = "l1";
		String label2 = "l2";

		Projection p = difference(projection(label1));
		assertEquals("difference(l1)", p.toString());

		p = difference(projection(label1), projection(label2));
		assertEquals("difference(l1, l2)", p.toString());

		p = difference(projection(label1), projection(label2).size()).as("c");
		assertEquals("difference(l1, l2.size()) as c", p.toString());
	}

	@Test
	public void dijkstraTest() {

		Projection p = dijkstra(Variable.current(), projection("#10:10"), "weight");
		assertEquals("dijkstra($current, #10:10, 'weight')", p.toString());

		p = dijkstra(Variable.current(), projection("#10:10"), "weight", Direction.BOTH);
		assertEquals("dijkstra($current, #10:10, 'weight', 'BOTH')", p.toString());

	}

	@Test
	public void distanceTest() {
		Projection p = distance(
				Variable.current().dot(projection("x")),
				Variable.current().dot(projection("y")),
				value(1.1),
				value(1.2)
		);
		assertEquals("distance($current.x, $current.y, 1.1, 1.2)", p.toString());

	}

	@Test
	public void distinctTest() {
		Projection p = distinct(Variable.current().dot(projection("x")));
		assertEquals("distinct($current.x)", p.toString());
	}

	@Test
	public void evalTest() {
		Projection p = eval("price * 120 / 100 - discount").as("finalPrice");
		assertEquals("eval('price * 120 / 100 - discount') as finalPrice", p.toString());
	}

	@Test
	public void expandTest() {
		Projection p = expand(out("field"));
		assertEquals("expand(out('field'))", p.toString());
	}

	@Test
	public void formatTest() {
		Projection p = ProjectionFunction.format("test: %s", out("edge").size());
		assertEquals("format('test: %s', out('edge').size())", p.toString());

		p = ProjectionFunction.format("'%s'", projection("field"));
		assertEquals("format('\\'%s\\'', field)", p.toString());
	}

	@Test
	public void firstTest() {
		Projection p = first(set(projection("field")));
		assertEquals("first(set(field))", p.toString());

		p.as("fi");
		assertEquals("first(set(field)) as fi", p.toString());
	}

	@Test
	public void gremlinTest() {
		Projection p = gremlin("current.as('id').outE.label.groupCount(map1).optional('id')");
		assertEquals("gremlin('current.as(\\'id\\').outE.label.groupCount(map1).optional(\\'id\\')')", p.toString());

		p = gremlin("current.as(\"id\").outE.label.groupCount(map1).optional(\"id\")");
		assertEquals("gremlin('current.as(\"id\").outE.label.groupCount(map1).optional(\"id\")')", p.toString());
	}

	@Test
	public void ifnullTest() {
		Projection p = ifnull("Not Found", first(projection("f1")), projection("f2"), projection("f3").index(0));
		assertEquals("ifnull(first(f1), f2, f3[0], 'Not Found')", p.toString());

		p.as("ifn");
		assertEquals("ifnull(first(f1), f2, f3[0], 'Not Found') as ifn", p.toString());
	}

	@Test
	public void inTest() {
		String label1 = "label1";
		String label2 = "label2";
		String label3 = "label3";

		Projection p = in(label1);
		assertEquals("in('label1')", p.toString());

		p = in(label1, label2, label3);
		p.as("x");
		assertEquals("in('label1', 'label2', 'label3') as x", p.toString());
	}

	@Test
	public void inETest() {
		String label1 = "label1";
		String label2 = "label2";

		Projection p = inE(label1);
		assertEquals("inE('label1')", p.toString());

		p = inE(label1, label1, label2);
		assertEquals("inE('label1', 'label1', 'label2')", p.toString());
	}

	@Test
	public void inVTest() {

		Projection p = inV();
		assertEquals("inV()", p.toString());

		p.as("inv");
		assertEquals("inV() as inv", p.toString());
	}

	@Test
	public void intersectTest() {

		Projection p = intersect(projection("simple"));
		assertEquals("intersect(simple)", p.toString());

		p = intersect(out("simple"), in("notSimple"));
		p.as("two");
		assertEquals("intersect(out('simple'), in('notSimple')) as two", p.toString());
	}

	@Test
	public void listTest() {

		Projection p = list(projection("simple"));
		assertEquals("list(simple)", p.toString());

		p.as("list");
		assertEquals("list(simple) as list", p.toString());
	}

	@Test
	public void mapTest() {

		Projection p = map(
				projection("name"),
				projection("roles").as("r").dot(projection("name"))
		);
		assertEquals("map(name, roles.name)", p.toString());
	}

	@Test
	public void maxTest() {

		Projection p = max(projection("simple"));
		assertEquals("max(simple)", p.toString());

		p = max(out("label"), projection("field"), projection("size").size());
		p.as("mixed");
		assertEquals("max(out('label'), field, size.size()) as mixed", p.toString());
	}

	@Test
	public void minTest() {

		Projection p = min(projection("simple"));
		p.as("alias");
		assertEquals("min(simple) as alias", p.toString());

		p = max(inV(), projection("field"), projection("first").index(0));
		p.as("mixed");
		assertEquals("max(inV(), field, first[0]) as mixed", p.toString());
	}

	@Test
	public void outTest() {
		String label1 = "label1";
		String label2 = "label2";
		String label3 = "label3";

		Projection p = out(label1);
		assertEquals("out('label1')", p.toString());

		p = out(label1, label2, label3);
		p.as("x");
		assertEquals("out('label1', 'label2', 'label3') as x", p.toString());
	}

	@Test
	public void outETest() {
		String label1 = "label1";
		String label2 = "label2";

		Projection p = outE(label1);
		assertEquals("outE('label1')", p.toString());

		p = outE(label1, label1, label2);
		assertEquals("outE('label1', 'label1', 'label2')", p.toString());
	}

	@Test
	public void outVTest() {

		Projection p = outV();
		assertEquals("outV()", p.toString());

		p.as("outv");
		assertEquals("outV() as outv", p.toString());
	}

	@Test
	public void setTest() {

		Projection p = set(projection("simple"));
		assertEquals("set(simple)", p.toString());

		p.as("set");
		assertEquals("set(simple) as set", p.toString());
	}

	@Test
	public void shortestPathTest() {

		Projection p = shortestPath(projection("#15:20"), out("friends").index(0));
		p.as("sp");
		assertEquals("shortestPath(#15:20, out('friends')[0]) as sp", p.toString());

		p = shortestPath(projection("#15:20"), out("friends").index(0), Direction.BOTH);
		p.as("sp");
		assertEquals("shortestPath(#15:20, out('friends')[0], 'BOTH') as sp", p.toString());

		p = shortestPath(projection("#15:20"), projection("#14:21"), Direction.IN);
		p.as("sp");
		assertEquals("shortestPath(#15:20, #14:21, 'IN') as sp", p.toString());

		p = shortestPath(out("friends").index(0), first(set(out("f1", "f2"))), Direction.OUT);
		p.as("sp");
		assertEquals("shortestPath(out('friends')[0], first(set(out('f1', 'f2'))), 'OUT') as sp", p.toString());
	}

	@Test
	public void sumTest() {

		Projection p = sum(projection("p"));
		assertEquals("sum(p)", p.toString());

		p = sum(out("friend").size(), out("enemy").size());
		p.as("degree");
		assertEquals("sum(out('friend').size(), out('enemy').size()) as degree", p.toString());
	}

	@Test
	public void sysdateTest() {

		Projection p = sysdate();
		assertEquals("sysdate()", p.toString());

		p = sysdate("dd");
		assertEquals("sysdate('dd')", p.toString());

		p = sysdate("hh", TimeZone.getTimeZone("America/New_York"));
		assertEquals("sysdate('hh', 'America/New_York')", p.toString());
	}

	@Test
	public void traversedElementTest() {

		Projection p = traversedElement(5);
		assertEquals("traversedElement(5)", p.toString());

		p = traversedElement(5, 10);
		assertEquals("traversedElement(5, 10)", p.toString());
	}

	@Test
	public void traversedEdgeTest() {

		Projection p = traversedEdge(-1);
		assertEquals("traversedEdge(-1)", p.toString());

		p = traversedEdge(-1, 10);
		assertEquals("traversedEdge(-1, 10)", p.toString());
	}

	@Test
	public void traversedVertexTest() {

		Projection p = traversedVertex(5);
		assertEquals("traversedVertex(5)", p.toString());

		p = traversedVertex(5, 10);
		p.as("tv");
		assertEquals("traversedVertex(5, 10) as tv", p.toString());
	}

	@Test
	public void unionTest() {

		Projection p = unionAll(projection("p"));
		assertEquals("unionAll(p)", p.toString());

		p = unionAll(out("friend"), out("enemy"));
		p.as("all");
		assertEquals("unionAll(out('friend'), out('enemy')) as all", p.toString());
	}

	@Test
	public void nestedTest() {

		Query innerQuery = new Query()
				.select("city")
				.select(sum(projection("salary")).as("salary"))
				.from("Employee")
				.groupBy("city");

		Projection p = nested(innerQuery);
		assertEquals("( SELECT city, sum(salary) as salary FROM Employee GROUP BY city )", p.toString());
	}

	@Test
	public void anyTest() {

		Projection p = any();
		assertEquals("any()", p.toString());

		p.as("a");
		assertEquals("any() as a", p.toString());
	}

	@Test
	public void allTest() {

		Projection p = all();
		assertEquals("all()", p.toString());

		p.as("a");
		assertEquals("all() as a", p.toString());
	}

	@Test
	public void joinTest() {

		Projection p = join(projection("p1"), projection("p2"));
		assertEquals("[p1, p2]", p.toString());

		p = join(projection("p1"), projection("p2"), projection("p3"), projection("p4"));
		assertEquals("[p1, p2, p3, p4]", p.toString());
	}

}