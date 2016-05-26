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

import com.github.raymanrt.orientqb.query.projection.AtomicProjection;
import com.github.raymanrt.orientqb.query.projection.CompositeProjection;
import com.github.raymanrt.orientqb.util.Commons;
import com.github.raymanrt.orientqb.util.Joiner;
import com.google.common.collect.Lists;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;

public class ProjectionFunction {

	// TODO: only in WHERE clause
	public static Projection column(int index) {
		return new CompositeProjection("column(%s)", Projection.value(index));
	}

	public static Projection avg(Projection firstProjection, Projection... projections) {
		List<Projection> allProjections = newArrayList(projections);
		allProjections.add(0, firstProjection);
		String projectionsString = Joiner.listJoiner.join(transform(newArrayList(allProjections), Commons.toStringFunction));
		return new CompositeProjection("avg(%s)", Projection.projection(projectionsString));
	}

	public static Projection both(String ... labels) {
		return new CompositeProjection("both(%s)", Projection.projection(Commons.joinStrings(labels)));
	}

	public static Projection bothE(String ... labels) {
		return new CompositeProjection("bothE(%s)", Projection.projection(Commons.joinStrings(labels)));
	}

	public static Projection coalesce(Projection... projections) {
		String projectionsString = Joiner.listJoiner.join(transform(newArrayList(projections), Commons.toStringFunction));
		return new CompositeProjection("coalesce(%s)", Projection.projection(projectionsString));
	}

	public static Projection count(Projection projection) {
		return new CompositeProjection("count(%s)", projection);
	}

	public static Projection date(Projection projection) {
		return new CompositeProjection("date(%s)", projection);
	}

	public static Projection date(Projection projection, SimpleDateFormat format) {
		return new CompositeProjection("date(%s, %s)", projection, Projection.value(format.toPattern()));

	}

	public static Projection date(Projection projection, SimpleDateFormat format, TimeZone timezone) {
		return new CompositeProjection("date(%s, %s, %s)",
				projection,
				Projection.value(format.toPattern()),
				Projection.value(timezone.getID())
		);

	}

	public static Projection difference(Projection firstProjection, Projection... projections) {
		List<Projection> allProjections = newArrayList(projections);
		allProjections.add(0, firstProjection);
		String projectionsString = Joiner.listJoiner.join(transform(newArrayList(allProjections), Commons.toStringFunction));
		return new CompositeProjection("difference(%s)", Projection.projection(projectionsString));
	}

	public static Projection dijkstra(Projection source, Projection destination, String weightEdgeFieldName) {
		String projectionsString = Joiner.listJoiner.join(transform(Lists.newArrayList(source, destination, Commons.cast(weightEdgeFieldName)), Commons.toStringFunction));
		return new CompositeProjection("dijkstra(%s)", Projection.projection(projectionsString));
	}

	public static Projection dijkstra(Projection source, Projection destination, String weightEdgeFieldName, Direction direction) {
		List<Object> arguments = Lists.newArrayList(source, destination, Commons.cast(weightEdgeFieldName), Commons.cast(direction.toString()));
		String projectionsString = Joiner.listJoiner.join(transform(arguments, Commons.toStringFunction));
		return new CompositeProjection("dijkstra(%s)", Projection.projection(projectionsString));
	}

	public static Projection distance(Projection latPoint1, Projection lonPoint1, Projection latPoint2, Projection lonPoint2) {
		return new CompositeProjection("distance(%s, %s, %s, %s)", latPoint1, lonPoint1, latPoint2, lonPoint2);
	}

	public static Projection distinct(Projection projection) {
		return new CompositeProjection("distinct(%s)", projection);
	}

	public static Projection eval(String expression) {
		return new CompositeProjection("eval('%s')", Projection.projection(expression));
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
		return new CompositeProjection("format('" + format + "', %s)", Projection.projection(Joiner.listJoiner.join(projections)));
	}

	private static String escape(String format) {
		return format.replace("%", "%%").replace("'", "\\'");
	}

	public static Projection first(Projection projection) {
		return new CompositeProjection("first(%s)", projection);
	}

	// WONTFIX: flatten is deprecated, why you should use it?

	public static Projection gremlin(String gremlin) {
		return new CompositeProjection("gremlin(%s)", Projection.projection(Commons.cast(gremlin)));
	}

	public static Projection ifnull(Object defaultValue, Projection... projections) {
		String projectionString = Joiner.listJoiner.join(transform(newArrayList(projections), Commons.toStringFunction));
		return new CompositeProjection("ifnull(%s, " + Commons.cast(defaultValue) +")", Projection.projection(projectionString));
	}

	public static Projection in(String ... labels) {
		return new CompositeProjection("in(%s)", Projection.projection(Commons.joinStrings(labels)));
	}

	public static Projection inE(String ... labels) {
		return new CompositeProjection("inE(%s)", Projection.projection(Commons.joinStrings(labels)));
	}

	public static Projection inV() {
		return new AtomicProjection("inV()");
	}

	public static Projection intersect(Projection firstProjection, Projection... projections) {
		List<Projection> allProjections = newArrayList(projections);
		allProjections.add(0, firstProjection);
		String projectionsString = Joiner.listJoiner.join(transform(newArrayList(allProjections), Commons.toStringFunction));
		return new CompositeProjection("intersect(%s)", Projection.projection(projectionsString));
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
		String projectionsString = Joiner.listJoiner.join(transform(newArrayList(allProjections), Commons.toStringFunction));
		return new CompositeProjection("max(%s)", Projection.projection(projectionsString));
	}

	public static Projection min(Projection firstProjection, Projection... projections) {
		List<Projection> allProjections = newArrayList(projections);
		allProjections.add(0, firstProjection);
		String projectionsString = Joiner.listJoiner.join(transform(newArrayList(allProjections), Commons.toStringFunction));
		return new CompositeProjection("min(%s)", Projection.projection(projectionsString));
	}

	public static Projection out(String ... labels) {
		return new CompositeProjection("out(%s)", Projection.projection(Commons.joinStrings(labels)));
	}

	public static Projection outE(String ... labels) {
		return new CompositeProjection("outE(%s)", Projection.projection(Commons.joinStrings(labels)));
	}

	public static Projection outV() {
		return new AtomicProjection("outV()");
	}

	public static Projection set(Projection projection) {
		return new CompositeProjection("set(%s)", projection);
	}

	public static Projection shortestPath(Projection source, Projection destination) {
		String projectionsString = Joiner.listJoiner.join(transform(newArrayList(source, destination), Commons.toStringFunction));
		return new CompositeProjection("shortestPath(%s)", Projection.projection(projectionsString));
	}

	public static Projection shortestPath(Projection source, Projection destination, Direction direction) {
		Projection directionAsProjection = Projection.projection("'" + direction.toString() + "'");
		String arguments = Joiner.listJoiner.join(transform(newArrayList(source, destination, directionAsProjection), Commons.toStringFunction));
		return new CompositeProjection("shortestPath(%s)", Projection.projection(arguments));
	}

	public static Projection sum(Projection firstProjection, Projection... projections) {
		List<Projection> allProjections = newArrayList(projections);
		allProjections.add(0, firstProjection);
		String projectionsString = Joiner.listJoiner.join(transform(newArrayList(allProjections), Commons.toStringFunction));
		return new CompositeProjection("sum(%s)", Projection.projection(projectionsString));
	}

	public static Projection sysdate() {
		return new AtomicProjection("sysdate()");
	}

	public static Projection sysdate(String format) {
		return new CompositeProjection("sysdate(%s)", Projection.value(format));
	}

	public static Projection sysdate(String format, TimeZone timezone) {
		String projectionsString = Joiner.listJoiner.join(transform(newArrayList(format, timezone.getID()), Commons.singleQuoteFunction));
		return new CompositeProjection("sysdate(%s)", Projection.projection(projectionsString));
	}

	public static Projection traversedElement(int index) {
		return new CompositeProjection("traversedElement(%s)", Projection.value(index));
	}

	public static Projection traversedElement(int index, int items) {
		String projectionString = Joiner.listJoiner.join(transform(newArrayList(index, items), Commons.toStringFunction));
		return new CompositeProjection("traversedElement(%s)", Projection.projection(projectionString));
	}

	public static Projection traversedEdge(int index) {
		return new CompositeProjection("traversedEdge(%s)", Projection.value(index));
	}

	public static Projection traversedEdge(int index, int items) {
		String projectionString = Joiner.listJoiner.join(transform(newArrayList(index, items), Commons.toStringFunction));
		return new CompositeProjection("traversedEdge(%s)", Projection.projection(projectionString));
	}

	public static Projection traversedVertex(int index) {
		return new CompositeProjection("traversedVertex(%s)", Projection.value(index));
	}

	public static Projection traversedVertex(int index, int items) {
		String projectionString = Joiner.listJoiner.join(transform(newArrayList(index, items), Commons.toStringFunction));
		return new CompositeProjection("traversedVertex(%s)", Projection.projection(projectionString));
	}

	public static Projection unionAll(Projection firstProjection, Projection... projections) {
		List<Projection> allProjections = newArrayList(projections);
		allProjections.add(0, firstProjection);
		String projectionsString = Joiner.listJoiner.join(transform(newArrayList(allProjections), Commons.toStringFunction));
		return new CompositeProjection("unionAll(%s)", Projection.projection(projectionsString));
	}

	//// SPECIAL ////

	public static Projection nested(Query query) {
		return new CompositeProjection("( %s )", Projection.projection(query.toString()));
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
		String projectionsString = Joiner.listJoiner.join(transform(newArrayList(allProjections), Commons.toStringFunction));
		return new CompositeProjection("[%s]", Projection.projection(projectionsString));
	}
}
