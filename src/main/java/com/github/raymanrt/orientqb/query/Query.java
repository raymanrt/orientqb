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
import com.github.raymanrt.orientqb.query.fetchingstrategy.FetchingStrategy;
import com.github.raymanrt.orientqb.util.Commons;
import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static com.github.raymanrt.orientqb.query.Projection.projection;
import static com.github.raymanrt.orientqb.util.Joiner.j;
import static com.github.raymanrt.orientqb.util.Joiner.listJoiner;
import static com.github.raymanrt.orientqb.util.Joiner.oneSpaceJoiner;
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

	private Set<Projection> projections = new LinkedHashSet<>();

	private Map<String, Assignable> letMap = newLinkedHashMap();

	private Set<Ordering> orderBy = newLinkedHashSet();

	private Optional<Projection> groupBy = Optional.absent();

	private Set<String> fetchPlan = newLinkedHashSet();

	private int skipValue = 0;
	private String skipVariable = "";

    public Query select(Projection s) {
        projections.add(s);
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

	@Override
	public Query fromEmpty() {
		super.fromEmpty();
		return this;
	}

	public Query from(Target target) {
		super.setTarget(target);
		return this;
	}

	public Query from(String target) {
		super.setTarget(target);
		return this;
	}

	@Override
	public Query where(Clause clause) {
		super.where(clause);
		return this;
	}

	public Query orderBy(String field) {
		return orderBy(projection(field));
	}

	public Query orderBy(Projection projection) {
		Ordering ordering = new Ordering(projection, Ordering.Order.ASC);
		addOrderBy(ordering);
		return this;
	}

	private void addOrderBy(Ordering ordering) {
		if(orderBy.contains(ordering)) {
			orderBy.remove(ordering);
		}
		orderBy.add(ordering);
	}

	public Query orderByDesc(String field) {
		return orderByDesc(projection(field));
	}

	public Query orderByDesc(Projection projection) {
		Ordering ordering = new Ordering(projection, Ordering.Order.DESC);
		addOrderBy(ordering);
		return this;
	}

	@Override
	public Query limit(int l) {
		super.limit(l);
		return this;
	}

	@Override
	public Query limit(String variable) {
		super.limit(variable);
		return this;
	}

	public Query fetchPlan(FetchingStrategy... strategies) {
		for(FetchingStrategy strategy : strategies) {
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
		letMap.put(j.join("$", variable), assignment);
		return this;
	}

	@Override
	public Query timeout(long timeoutInMS, TimeoutStrategy strategy) {
		super.timeout(timeoutInMS, strategy);
		return this;
	}

	@Override
	public Query timeout(long timeoutInMS) {
		super.timeout(timeoutInMS);
		return this;
	}

	public Query lockRecord() {
		super.lock(LockingStrategy.RECORD);
		return this;
	}

	@Override
	public Query lockReset() {
		super.lockReset();
		return this;
	}

	@Override
	public Query lock(LockingStrategy strategy) {
		super.lock(strategy);
		return this;
	}

	@Override
	public Query parallel() {
		super.parallel();
		return this;
	}

	@Override
	public Query parallel(boolean parallel) {
		super.parallel(parallel);
		return this;
	}

	public String toString() {
		StringBuilder baseBuilder = new StringBuilder(SELECT).append(" %s ");

		if(!getTarget().equals(Target.EMPTY)) {
			baseBuilder.append(" ").append(FROM).append(" %s ");
		}

		String joinedProjections = joinProjections();
		StringBuilder query = new StringBuilder(format(baseBuilder.toString(), joinedProjections, getTarget()))

			.append(joinLet())
			.append(joinWhere())
			.append(generateGroupBy())
			.append(joinOrderBy())
			.append(generateSkip())
			.append(generateLimit())

			.append(generateFetchPlan())
			.append(generateTimeout())
			.append(generateLock())
			.append(generateParallel());

		return Commons.clean(query.toString());
	}

	private String joinProjections() {
		return listJoiner.join(projections);
	}

	private String joinLet() {
		if(letMap.size() > 0) {

			List<String> letAssignments = new ArrayList<>();
			for(Entry<String, Assignable> letClause : letMap.entrySet()) {
				Assignable assignable = letClause.getValue();
				String assignment = assignable.getAssignment();
				if(assignment.toUpperCase().startsWith(SELECT)) {
					assignment = j.join("( ", assignment, " ", ")");
				}

				letAssignments.add(format("%s = %s", letClause.getKey(), assignment));
			}

			return j.join(LET, " ",
					listJoiner.join(letAssignments), " ");
		}
		return "";
	}

	private String generateGroupBy() {
		if(groupBy.isPresent()) {
			return j.join(" ", GROUP_BY,
					" ", groupBy.get().getAssignment(), " ");
		}
		return "";
	}

	private String joinOrderBy() {
		if(orderBy.isEmpty()) {
			return "";
		}
		String flattenOrderBy = listJoiner.join(orderBy);
		return j.join(" ", ORDER_BY, " ", flattenOrderBy, " ");
	}

	private String generateSkip() {

		StringBuilder skipPart = new StringBuilder();
		if(Commons.validVariable(skipVariable)) {
			skipPart.append(" ").append(SKIP).append(" ").append(skipVariable);
		}
		if(skipValue > 0) {
			skipPart = new StringBuilder()
					.append(" ").append(SKIP).append(" ")
					.append(Integer.toString(skipValue));
		}
		return skipPart.toString();
	}

	private String generateFetchPlan() {
		if(fetchPlan.isEmpty()) {
			return "";
		}
		String fetchPlanString = oneSpaceJoiner.join(fetchPlan);
		return j.join(" ", FETCHPLAN, " ", fetchPlanString, " ");
	}

	@Override
	public String getAssignment() {
		return toString();
	}
}
