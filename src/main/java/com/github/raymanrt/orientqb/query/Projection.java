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

import com.github.raymanrt.orientqb.query.clause.AtomicClause;
import com.github.raymanrt.orientqb.query.clause.CustomFormatClause;
import com.github.raymanrt.orientqb.query.projection.AtomicProjection;
import com.github.raymanrt.orientqb.query.projection.CompositeProjection;
import com.github.raymanrt.orientqb.util.Commons;
import com.github.raymanrt.orientqb.util.Patterns;
import com.google.common.base.Function;
import com.google.common.base.Optional;

import java.security.MessageDigest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.raymanrt.orientqb.util.Commons.arrayToString;
import static com.github.raymanrt.orientqb.util.Commons.cast;
import static com.github.raymanrt.orientqb.util.Commons.singleQuote;
import static com.github.raymanrt.orientqb.util.Commons.singleQuoteFunction;
import static com.github.raymanrt.orientqb.util.Joiner.listJoiner;
import static com.github.raymanrt.orientqb.util.Joiner.oneCommaJoiner;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;

public abstract class Projection implements Assignable {

	protected Optional<String> alias = Optional.absent();

	public static final Projection ALL = Projection.projection("*");
	private static final Function<Number[], Projection> toProjection = new Function<Number[], Projection>() {
		@Override
		public Projection apply(Number[] input) {
			return projection(arrayToString(input));
		}
	};

	public static Projection projection(String field) {
		return new AtomicProjection(field);
	}

	public static Projection value(Object value) {
		return new AtomicProjection(cast(value));
	}

	public static Projection list(Object ... value) {
		return new AtomicProjection(cast(newArrayList(value)));
	}

	public String getName() {
		if(alias.isPresent())
			return alias.get();
		return generatedAlias(toString());
	}

	private String generatedAlias(String s) {
		Matcher m = Patterns.IDENTIFIER.matcher(s);
		boolean found = m.find();
		if(found)
			return m.group();
		return "";
	}

	public Projection as(String alias) {
		this.alias = Optional.of(alias);
		return this;
	}

	//// BASE EXPRESSIONS ////

	public Projection dot(Projection projection) {
		return new CompositeProjection("%s.%s", this, projection);
	}

//	public Projection index(int index) {
//		return new CompositeProjection("%s[%s]", this, value(index));
//	}

	public Projection index(Integer ... indices) {
		return new CompositeProjection("%s%s", this, projection(Commons.arrayToString(indices)));
	}

	public Projection indexRange(int from, int to) {
		return new CompositeProjection("%s[%s-%s]", this, value(from), value(to));
	}

	public Projection field(String ... names) {
		return new CompositeProjection("%s%s", this, projection(Commons.arrayToString(names)));
	}

	//// METHODS ////

	public Projection append(String constant) {
		return new CompositeProjection("%s.append('%s')", this, projection(constant));
	}

	public Projection append(Projection projection) {
		return new CompositeProjection("%s.append(%s)", this, projection);
	}

	public Projection asBoolean() {
		return new CompositeProjection("%s.asBoolean()", this);
	}

	public Projection asDate() {
		return new CompositeProjection("%s.asDate()", this);
	}

	public Projection asDateTime() {
		return new CompositeProjection("%s.asDateTime()", this);
	}

	public Projection asDecimal() {
		return new CompositeProjection("%s.asDecimal()", this);
	}

	public Projection asFloat() {
		return new CompositeProjection("%s.asFloat()", this);
	}

	public Projection asInteger() {
		return new CompositeProjection("%s.asInteger()", this);
	}

	public Projection asList() {
		return new CompositeProjection("%s.asList()", this);
	}

	public Projection asLong() {
		return new CompositeProjection("%s.asLong()", this);
	}

	public Projection asMap() {
		return new CompositeProjection("%s.asMap()", this);
	}

	public Projection asSet() {
		return new CompositeProjection("%s.asSet()", this);
	}

	public Projection asString() {
		return new CompositeProjection("%s.asString()", this);
	}

	public Projection both(String ... labels) {
		String argumentsString = listJoiner.join(transform(newArrayList(labels), singleQuoteFunction));
		Projection arguments = projection(argumentsString);
		return new CompositeProjection("%s.both(%s)", this, arguments);
	}

	public Projection bothE(String ... labels) {
		String argumentsString = listJoiner.join(transform(newArrayList(labels), singleQuoteFunction));
		Projection arguments = projection(argumentsString);
		return new CompositeProjection("%s.bothE(%s)", this, arguments);
	}

	public Projection charAt(int position) {
		return new CompositeProjection("%s.charAt(%s)", this, value(position));
	}

	public Projection convert(DataType type) {
		return new CompositeProjection("%s.convert('%s')", this, projection(type.toString()));
	}

	public Projection exclude(String ... fieldNames) {
		String argumentsString = listJoiner.join(transform(newArrayList(fieldNames), singleQuoteFunction));
		Projection arguments = projection(argumentsString);
		return new CompositeProjection("%s.exclude(%s)", this, arguments);
	}

	public Projection format(String format) {
		return new CompositeProjection("%s.format(%s)", this, value(format));
	}

	public Projection hash() {
		return new CompositeProjection("%s.hash()", this);
	}

	public Projection hash(MessageDigest algorithm) {
		return new CompositeProjection("%s.hash('%s')", this, projection(algorithm.getAlgorithm()));
	}

	public Projection in(String ... labels) {
		String argumentsString = listJoiner.join(transform(newArrayList(labels), singleQuoteFunction));
		Projection arguments = projection(argumentsString);
		return new CompositeProjection("%s.in(%s)", this, arguments);
	}

	public Projection inE(String ... labels) {
		String argumentsString = listJoiner.join(transform(newArrayList(labels), singleQuoteFunction));
		Projection arguments = projection(argumentsString);
		return new CompositeProjection("%s.inE(%s)", this, arguments);
	}

	public Projection inV(String ... labels) {
		String argumentsString = listJoiner.join(transform(newArrayList(labels), singleQuoteFunction));
		Projection arguments = projection(argumentsString);
		return new CompositeProjection("%s.inV(%s)", this, arguments);
	}

	public Projection include(String ... fieldNames) {
		String argumentsString = listJoiner.join(transform(newArrayList(fieldNames), singleQuoteFunction));
		Projection arguments = projection(argumentsString);
		return new CompositeProjection("%s.include(%s)", this, arguments);
	}

	public Projection indexOf(String toSearch) {
		return new CompositeProjection("%s.indexOf(%s)", this, projection(singleQuote(toSearch)));
	}

	public Projection indexOf(String toSearch, int begin) {
		return new CompositeProjection("%s.indexOf(%s, %s)", this, value(toSearch), value(begin));
	}

	public Projection javaType() {
		return new CompositeProjection("%s.javaType()", this);
	}

	public Projection keys() {
		return new CompositeProjection("%s.keys()", this);
	}

	public Projection left(int length) {
		return new CompositeProjection("%s.left(%s)", this, value(length));
	}

	public Projection length() {
		return new CompositeProjection("%s.length()", this);
	}

	public Projection normalize() {
		return new CompositeProjection("%s.normalize()", this);
	}

	public Projection normalize(String form) {
		return new CompositeProjection("%s.normalize(%s)", this, value(form));
	}

	public Projection normalize(String form, String patternMatching) {
		return new CompositeProjection("%s.normalize(%s, %s)", this, value(form), value(patternMatching));
	}

	public Projection out(String ... labels) {
		String argumentsString = listJoiner.join(transform(newArrayList(labels), singleQuoteFunction));
		Projection arguments = projection(argumentsString);
		return new CompositeProjection("%s.out(%s)", this, arguments);
	}

	public Projection outE(String ... labels) {
		String argumentsString = listJoiner.join(transform(newArrayList(labels), singleQuoteFunction));
		Projection arguments = projection(argumentsString);
		return new CompositeProjection("%s.outE(%s)", this, arguments);
	}

	public Projection outV(String ... labels) {
		String argumentsString = listJoiner.join(transform(newArrayList(labels), singleQuoteFunction));
		Projection arguments = projection(argumentsString);
		return new CompositeProjection("%s.outV(%s)", this, arguments);
	}

	public Projection prefix(String constant) {
		return new CompositeProjection("%s.prefix('%s')", this, projection(constant));
	}

	public Projection prefix(Projection projection) {
		return new CompositeProjection("%s.prefix(%s)", this, projection);
	}

	public Projection remove(Projection ... projections) {
		List<Projection> projectionsList = newArrayList(projections);
		projectionsList.add(0, this);
		return new CompositeProjection("%s.remove(%s)", projectionsList.toArray(new Projection[]{}));
	}

	public Projection removeAll(Projection ... projections) {
		List<Projection> projectionsList = newArrayList(projections);
		projectionsList.add(0, this);
		return new CompositeProjection("%s.removeAll(%s)", projectionsList.toArray(new Projection[]{}));
	}

	public Projection replace(String toFind, String toReplace) {
		return new CompositeProjection("%s.replace(%s, %s)", this, value(toFind), value(toReplace));
	}

	public Projection right(int length) {
		return new CompositeProjection("%s.right(%s)", this, value(length));
	}

	public Projection size() {
		return new CompositeProjection("%s.size()", this);
	}

	public Projection subString(int begin) {
		return new CompositeProjection("%s.subString(%s)", this, value(begin));
	}

	public Projection subString(int begin, int length) {
		return new CompositeProjection("%s.subString(%s, %s)", this, value(begin), value(length));
	}

	public Projection trim() {
		return new CompositeProjection("%s.trim()", this);
	}

	public Projection toJson() {
		return new CompositeProjection("%s.toJson()", this);
	}

	public Projection toJson(String format) {
		return new CompositeProjection("%s.toJson('%s')", this, new AtomicProjection(format));
	}

	public Projection toJson(Projection... projections) {
		return new CompositeProjection("%s.toJson('%s')", this, projection(oneCommaJoiner.join(projections)));
	}

	public Projection toLowerCase() {
		return new CompositeProjection("%s.toLowerCase()", this);
	}

	public Projection toUpperCase() {
		return new CompositeProjection("%s.toUpperCase()", this);
	}

	public Projection type() {
		return new CompositeProjection("%s.type()", this);
	}

	public Projection values() {
		return new CompositeProjection("%s.values()", this);
	}

	//// MATH OPERATORS ////

	public Projection plus(Number number) {
		return new CompositeProjection("%s + %s", this, value(number));
	}
	public Projection minus(Number number) {
		return new CompositeProjection("%s - %s", this, value(number));
	}
	public Projection times(Number number) {
		return new CompositeProjection("%s * %s", this, value(number));
	}
	public Projection divide(Number number) {
		return new CompositeProjection("%s / %s", this, value(number));
	}
	public Projection mod(int number) {
		return new CompositeProjection("%s %% %s", this, value(number));
	}

    public Projection field(Clause clause) {
        return new CompositeProjection("%s[%s]", this, value(clause));
    }

	//// CLAUSES ////

	public Clause field(String field, Object value) {
		return new CustomFormatClause(Operator.FIELD, this, field, cast(value));
	}

	public Clause field(Projection projection, Object value) {
		return new CustomFormatClause(Operator.FIELD, this, projection, cast(value));
	}

	public Clause eq(Object value) {
		return new AtomicClause(this, Operator.EQ, value);
	}

	public Clause ne(Object value) {
		return new AtomicClause(this, Operator.NE, value);
	}

	public Clause lt(Object value) {
		return new AtomicClause(this, Operator.LT, value);
	}

	public Clause le(Object value) {
		return new AtomicClause(this, Operator.LE, value);
	}

	public Clause gt(Object value) {
		return new AtomicClause(this, Operator.GT, value);
	}

	public Clause ge(Object value) {
		return new AtomicClause(this, Operator.GE, value);
	}

	public Clause like(Object value) {
		return new AtomicClause(this, Operator.LIKE, value);
	}

    public Clause instanceOf(DataType dataType) {
        return new AtomicClause(this, Operator.INSTANCEOF, dataType.toString());
    }

    public Clause instanceOf(String className) {
        return new AtomicClause(this, Operator.INSTANCEOF, className);
    }

	public Clause in(Projection value) {
		return new AtomicClause(this, Operator.IN, value);
	}

	public Clause contains(Projection projection) {
		return new AtomicClause(this, Operator.CONTAINS, projection);
	}

	public Clause containsKey(String key) {
		return new AtomicClause(this, Operator.CONTAINS_KEY, key);
	}

	public Clause containsValue(Object value) {
		return new AtomicClause(this, Operator.CONTAINS_VALUE, value);
	}

	public Clause containsText(Projection projection) {
		return new AtomicClause(this, Operator.CONTAINS_TEXT, projection);
	}

	public Clause matches(Pattern pattern) {
		return new AtomicClause(this, Operator.MATCHES, pattern.toString());
	}

	public Clause lucene(String query) {
		return new AtomicClause(this, Operator.LUCENE, query);
	}

	public Clause near(Number lat, Number lon) {
		Projection joined = ProjectionFunction.join(projection(lat.toString()), projection(lon.toString()));
		return new AtomicClause(this, Operator.NEAR, joined);
	}

	public Clause near(Number lat, Number lon, Number distanceInKM) {
		Projection joined = ProjectionFunction.join(
				projection(lat.toString()),
				projection(lon.toString()),
				new CompositeProjection("{'maxDistance': %s}", projection(distanceInKM.toString()))
		);
		return new AtomicClause(this, Operator.NEAR, joined);
	}

	public Clause near(Number lat, Number lon, Projection projection) {
		Projection joined = ProjectionFunction.join(
				projection(lat.toString()),
				projection(lon.toString()),
				new CompositeProjection("{'maxDistance': %s}", projection)
		);
		return new AtomicClause(this, Operator.NEAR, joined);
	}

	public Clause within(Number[] ... points) {

		List<Number[]> pointsToList = newArrayList(points);
		List<Projection> projections = transform(pointsToList, toProjection);
		Projection joined = ProjectionFunction.join(
				projections.get(0),
				projections.get(1),
				projections.subList(2, projections.size()).toArray(new Projection[]{})
		);
		return new AtomicClause(this, Operator.WITHIN, joined);
	}

	public Clause between(Number from, Number to) {
		return new CustomFormatClause(Operator.BETWEEN, this, cast(from), cast(to));
	}

    public Clause defined(){
        return new CustomFormatClause(Operator.DEFINED, this);
    }

    public Clause notDefined(){
        return new CustomFormatClause(Operator.NOT_DEFINED, this);
    }

    public Clause isNull(){
        return new CustomFormatClause(Operator.NULL, this);
    }

    public Clause notNull(){
        return new CustomFormatClause(Operator.NOT_NULL, this);
    }
}
