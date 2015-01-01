package it.celi.orient.query;

import it.celi.orient.query.projection.AtomicProjection;
import it.celi.orient.query.projection.CompositeProjection;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;
import static it.celi.orient.query.Projection.projection;
import static it.celi.orient.query.Projection.value;
import static it.celi.orient.util.Commons.cast;
import static it.celi.orient.util.Commons.joinStrings;
import static it.celi.orient.util.Commons.singleQuoteFunction;
import static it.celi.orient.util.Commons.toStringFunction;
import static it.celi.orient.util.Joiner.listJoiner;

public class ProjectionFunction {

	// TODO: only in WHERE clause
	public static Projection column(int index) {
		return new CompositeProjection("column(%s)", value(index));
	}

	public static Projection avg(Projection firstProjection, Projection... projections) {
		List<Projection> allProjections = newArrayList(projections);
		allProjections.add(0, firstProjection);
		String projectionsString = listJoiner.join(transform(newArrayList(allProjections), toStringFunction));
		return new CompositeProjection("avg(%s)", projection(projectionsString));
	}

	public static Projection both(String ... labels) {
		return new CompositeProjection("both(%s)", projection(joinStrings(labels)));
	}

	public static Projection bothE(String ... labels) {
		return new CompositeProjection("bothE(%s)", projection(joinStrings(labels)));
	}

	public static Projection coalesce(Projection... projections) {
		String projectionsString = listJoiner.join(transform(newArrayList(projections), toStringFunction));
		return new CompositeProjection("coalesce(%s)", projection(projectionsString));
	}

	public static Projection count(Projection projection) {
		return new CompositeProjection("count(%s)", projection);
	}

	public static Projection date(Projection projection) {
		return new CompositeProjection("date(%s)", projection);
	}

	public static Projection date(Projection projection, SimpleDateFormat format) {
		return new CompositeProjection("date(%s, %s)", projection, value(format.toPattern()));

	}

	public static Projection date(Projection projection, SimpleDateFormat format, TimeZone timezone) {
		return new CompositeProjection("date(%s, %s, %s)",
				projection,
				value(format.toPattern()),
				value(timezone.getID())
		);

	}

	public static Projection difference(Projection firstProjection, Projection... projections) {
		List<Projection> allProjections = newArrayList(projections);
		allProjections.add(0, firstProjection);
		String projectionsString = listJoiner.join(transform(newArrayList(allProjections), toStringFunction));
		return new CompositeProjection("difference(%s)", projection(projectionsString));
	}

	public static Projection dijkstra(Projection source, Projection destination, String weightEdgeFieldName) {
		String projectionsString = listJoiner.join(transform(newArrayList(source, destination, cast(weightEdgeFieldName)), toStringFunction));
		return new CompositeProjection("dijkstra(%s)", projection(projectionsString));
	}

	public static Projection dijkstra(Projection source, Projection destination, String weightEdgeFieldName, Direction direction) {
		List<Object> arguments = newArrayList(source, destination, cast(weightEdgeFieldName), cast(direction.toString()));
		String projectionsString = listJoiner.join(transform(arguments, toStringFunction));
		return new CompositeProjection("dijkstra(%s)", projection(projectionsString));
	}

	public static Projection distance(Projection latPoint1, Projection lonPoint1, Projection latPoint2, Projection lonPoint2) {
		return new CompositeProjection("distance(%s, %s, %s, %s)", latPoint1, lonPoint1, latPoint2, lonPoint2);
	}

	public static Projection distinct(Projection projection) {
		return new CompositeProjection("distinct(%s)", projection);
	}

	public static Projection eval(String expression) {
		return new CompositeProjection("eval('%s')", projection(expression));
	}

	public static Projection expand(Projection projection) {
		return new CompositeProjection("expand(%s)", projection);
	}

	/**
	 *
	 * @param format
	 * @param projections: please note that no constant could be used as a projection
	 *                   if you need some constant, probably you can add it to the format
	 * @return
	 */
	public static Projection format(String format, Projection... projections) {
		format = escape(format);
		return new CompositeProjection("format('" + format + "', %s)", projection(listJoiner.join(projections)));
	}

	private static String escape(String format) {
		return format.replace("%", "%%").replace("'", "\\'");
	}

	public static Projection first(Projection projection) {
		return new CompositeProjection("first(%s)", projection);
	}

	// WONTFIX: flatten is deprecated, why you should use it?

	public static Projection ifnull(Object defaultValue, Projection... projections) {
		String projectionString = listJoiner.join(transform(newArrayList(projections), toStringFunction));
		return new CompositeProjection("ifnull(%s, " + cast(defaultValue) +")", projection(projectionString));
	}

	public static Projection in(String ... labels) {
		return new CompositeProjection("in(%s)", projection(joinStrings(labels)));
	}

	public static Projection inE(String ... labels) {
		return new CompositeProjection("inE(%s)", projection(joinStrings(labels)));
	}

	public static Projection inV() {
		return new AtomicProjection("inV()");
	}

	public static Projection intersect(Projection firstProjection, Projection... projections) {
		List<Projection> allProjections = newArrayList(projections);
		allProjections.add(0, firstProjection);
		String projectionsString = listJoiner.join(transform(newArrayList(allProjections), toStringFunction));
		return new CompositeProjection("intersect(%s)", projection(projectionsString));
	}

	public static Projection list(Projection projection) {
		return new CompositeProjection("list(%s)", projection);
	}

	public static Projection map(Projection key, Projection value) {
		return new CompositeProjection("map(%s, %s)", key, value);
	}
	public static Projection max(Projection firstProjection, Projection... projections) {
		List<Projection> allProjections = newArrayList(projections);
		allProjections.add(0, firstProjection);
		String projectionsString = listJoiner.join(transform(newArrayList(allProjections), toStringFunction));
		return new CompositeProjection("max(%s)", projection(projectionsString));
	}

	public static Projection min(Projection firstProjection, Projection... projections) {
		List<Projection> allProjections = newArrayList(projections);
		allProjections.add(0, firstProjection);
		String projectionsString = listJoiner.join(transform(newArrayList(allProjections), toStringFunction));
		return new CompositeProjection("min(%s)", projection(projectionsString));
	}

	public static Projection out(String ... labels) {
		return new CompositeProjection("out(%s)", projection(joinStrings(labels)));
	}

	public static Projection outE(String ... labels) {
		return new CompositeProjection("outE(%s)", projection(joinStrings(labels)));
	}

	public static Projection outV() {
		return new AtomicProjection("outV()");
	}

	public static Projection set(Projection projection) {
		return new CompositeProjection("set(%s)", projection);
	}

	public static Projection shortestPath(Projection source, Projection destination) {
		String projectionsString = listJoiner.join(transform(newArrayList(source, destination), toStringFunction));
		return new CompositeProjection("shortestPath(%s)", projection(projectionsString));
	}

	public static Projection shortestPath(Projection source, Projection destination, Direction direction) {
		Projection directionAsProjection = projection("'" + direction.toString() + "'");
		String arguments = listJoiner.join(transform(newArrayList(source, destination, directionAsProjection), toStringFunction));
		return new CompositeProjection("shortestPath(%s)", projection(arguments));
	}

	public static Projection sum(Projection firstProjection, Projection... projections) {
		List<Projection> allProjections = newArrayList(projections);
		allProjections.add(0, firstProjection);
		String projectionsString = listJoiner.join(transform(newArrayList(allProjections), toStringFunction));
		return new CompositeProjection("sum(%s)", projection(projectionsString));
	}

	public static Projection sysdate() {
		return new AtomicProjection("sysdate()");
	}

	public static Projection sysdate(String format) {
		return new CompositeProjection("sysdate(%s)", value(format));
	}

	public static Projection sysdate(String format, TimeZone timezone) {
		String projectionsString = listJoiner.join(transform(newArrayList(format, timezone.getID()), singleQuoteFunction));
		return new CompositeProjection("sysdate(%s)", projection(projectionsString));
	}

	public static Projection traversedElement(int index) {
		return new CompositeProjection("traversedElement(%s)", value(index));
	}

	public static Projection traversedElement(int index, int items) {
		String projectionString = listJoiner.join(transform(newArrayList(index, items), toStringFunction));
		return new CompositeProjection("traversedElement(%s)", projection(projectionString));
	}

	public static Projection traversedEdge(int index) {
		return new CompositeProjection("traversedEdge(%s)", value(index));
	}

	public static Projection traversedEdge(int index, int items) {
		String projectionString = listJoiner.join(transform(newArrayList(index, items), toStringFunction));
		return new CompositeProjection("traversedEdge(%s)", projection(projectionString));
	}

	public static Projection traversedVertex(int index) {
		return new CompositeProjection("traversedVertex(%s)", value(index));
	}

	public static Projection traversedVertex(int index, int items) {
		String projectionString = listJoiner.join(transform(newArrayList(index, items), toStringFunction));
		return new CompositeProjection("traversedVertex(%s)", projection(projectionString));
	}

	public static Projection union(Projection firstProjection, Projection... projections) {
		List<Projection> allProjections = newArrayList(projections);
		allProjections.add(0, firstProjection);
		String projectionsString = listJoiner.join(transform(newArrayList(allProjections), toStringFunction));
		return new CompositeProjection("union(%s)", projection(projectionsString));
	}

	//// SPECIAL ////

	public static Projection nested(Query query) {
		return new CompositeProjection("( %s )", projection(query.toString()));
	}

	//// ONLY IN WHERE ////
	public static Projection any() {
		return new AtomicProjection("any()");
	}
	public static Projection all() {
		return new AtomicProjection("all()");
	}

	// AS SEEN ON: http://www.orientechnologies.com/docs/1.7.8/orientdb-lucene.wiki/Full-Text-Index.html
	public static Projection join(Projection firstProjection, Projection secondProjection, Projection... projections) {
		List<Projection> allProjections = newArrayList(projections);
		allProjections.add(0, secondProjection);
		allProjections.add(0, firstProjection);
		String projectionsString = listJoiner.join(transform(newArrayList(allProjections), toStringFunction));
		return new CompositeProjection("[%s]", projection(projectionsString));
	}
}