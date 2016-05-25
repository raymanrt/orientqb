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

import com.github.raymanrt.orientqb.query.core.AbstractQuery;
import com.github.raymanrt.orientqb.util.Commons;
import com.github.raymanrt.orientqb.util.Joiner;
import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static com.github.raymanrt.orientqb.query.Projection.projection;
import static com.github.raymanrt.orientqb.util.Token.FETCHPLAN;
import static com.github.raymanrt.orientqb.util.Token.FROM;
import static com.github.raymanrt.orientqb.util.Token.GROUP_BY;
import static com.github.raymanrt.orientqb.util.Token.LET;
import static com.github.raymanrt.orientqb.util.Token.ORDER_BY;
import static com.github.raymanrt.orientqb.util.Token.SELECT;
import static com.github.raymanrt.orientqb.util.Token.SKIP;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static com.google.common.collect.Sets.newLinkedHashSet;
import static java.lang.String.format;

public class Query extends AbstractQuery implements Assignable {

	private Set<Projection> projections = new LinkedHashSet<Projection>();
	private boolean selectAll = false;

	private Map<String, Assignable> letMap = newLinkedHashMap();

	private Set<Ordering> orderBy = newLinkedHashSet();

	private Optional<Projection> groupBy = Optional.absent();

	private Set<String> fetchPlan = newLinkedHashSet();

	private int skipValue = 0;
	private String skipVariable = "";


	public Query select(Projection s) {
		if(s.equals(Projection.ALL)) selectAll = true;
		if(!selectAll) projections.add(s);
		return this;
	}

	public Query select(String s) {
		this.select(projection(s));
		return this;
	}

	public Query select(Projection... projections) {
		for(Projection s : projections)
			this.select(s);
		return this;
	}

	public Query select(String ... projections) {
		for(String projection : projections)
			this.select(projection(projection));
		return this;
	}

	public Query fromEmpty() {
		super.fromEmpty();
		return this;
	}

	public Query from(Target target) {
		super.from(target);
		return this;
	}

	public Query from(String target) {
		super.from(target);
		return this;
	}

	public Query where(Clause clause) {
		super.where(clause);
		return this;
	}

	public Query orderBy(String field) {
		orderBy.add(new Ordering(projection(field), Ordering.Order.ASC));
		return this;
	}

	public Query orderBy(Projection projection) {
		orderBy.add(new Ordering(projection, Ordering.Order.ASC));
		return this;
	}

	public Query orderByDesc(String field) {
		orderBy.add(new Ordering(projection(field), Ordering.Order.DESC));
		return this;
	}

	public Query orderByDesc(Projection projection) {
		orderBy.add(new Ordering(projection, Ordering.Order.DESC));
		return this;
	}

	public Query limit(int l) {
		super.limit(l);
		return this;
	}

	public Query limit(String variable) {
		super.limit(variable);
		return this;
	}

	public Query fetchPlan(FetchingStrategy... strategies) {
		for(Strategy strategy : strategies) {
			fetchPlan.add(strategy.toString());
		}
		return this;
	}

	public Query skip(int toSkip) {
		this.skipValue = toSkip;
		return this;
	}

	public Query skip(String variable) {
		this.skipVariable = variable;
		return this;
	}

	public Query groupBy(String field) {
		groupBy = Optional.of(projection(field));
		return this;
	}

	public Query groupBy(Projection projection) {
		groupBy = Optional.of(projection);
		return this;
	}

	public Query let(String variable, Assignable assignment) {
		letMap.put("$" + variable, assignment);
		return this;
	}

	public Query timeout(long timeoutInMS, TimeoutStrategy strategy) {
		super.timeout(timeoutInMS, strategy);
		return this;
	}

	public Query timeout(long timeoutInMS) {
		super.timeout(timeoutInMS);
		return this;
	}

	public Query lockRecord() {
		super.lock(LockingStrategy.RECORD);
		return this;
	}

	public Query lockReset() {
		super.lockReset();
		return this;
	}

	public Query lock(LockingStrategy strategy) {
		super.lock(strategy);
		return this;
	}

	public Query parallel() {
		super.parallel();
		return this;
	}

	public Query parallel(boolean parallel) {
		super.parallel(parallel);
		return this;
	}

	public String toString() {
		String base = SELECT + " %s ";

		if(!getTarget().equals(Target.EMPTY)) {
			base +=  " " + FROM + " %s ";
		}

		String joinedProjections = joinProjections();
		String query = format(base, joinedProjections, getTarget());

		query += joinLet();
		query += joinWhere();
		query += generateGroupBy();
		query += joinOrderBy();
		query += generateSkip();
		query += generateLimit();

		query += generateFetchPlan();
		query += generateTimeout();
		query += generateLock();
		query += generateParallel();

		return Commons.clean(query);
	}

	private String joinProjections() {
		String flattenSelect = Joiner.listJoiner.join(projections);
		if(selectAll) flattenSelect = Projection.ALL.getName();
		return flattenSelect;
	}

	private String joinLet() {
		if(letMap.size() > 0) {

			List<String> letAssignments = new ArrayList<>();
			for(Entry<String, Assignable> letClause : letMap.entrySet()) {
				Assignable assignable = letClause.getValue();
				String assignment = assignable.getAssignment();
				if(assignment.toUpperCase().startsWith(SELECT))
					assignment = "( " + assignment + " )";

				letAssignments.add(String.format("%s = %s", letClause.getKey(), assignment));
			}

			return LET + " " + Joiner.listJoiner.join(letAssignments) + " ";
		}
		return "";
	}

	private String generateGroupBy() {
		if(groupBy.isPresent()) {
			return " " + GROUP_BY + " " + groupBy.get().getAssignment() + " ";
		}
		return "";
	}

	private String joinOrderBy() {
		if(orderBy.size() > 0) {
			String flattenOrderBy = Joiner.listJoiner.join(orderBy);
			return " " + ORDER_BY + " " + flattenOrderBy + " ";
		}
		return "";
	}

	private String generateSkip() {

		String skipPart = "";
		if(Commons.validVariable(skipVariable)) {
			skipPart = " " + SKIP + " " + skipVariable;
		}
		if(skipValue > 0) {
			skipPart = " " + SKIP + " " + Integer.toString(skipValue);
		}
		if(!skipPart.isEmpty())
			return skipPart;
		return "";
	}

	private String generateFetchPlan() {
		if(fetchPlan.size() > 0) {
			String fetchPlanString = Joiner.oneSpaceJoiner.join(fetchPlan);
			return " " + FETCHPLAN + " " + fetchPlanString + " ";
		}
		return "";
	}

	@Override
	public String getAssignment() {
		return toString();
	}
}
