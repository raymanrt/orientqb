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

package it.raymanrt.orient.query;

import com.google.common.base.Optional;
import it.raymanrt.orient.query.clause.CompositeClause;
import it.raymanrt.orient.util.Commons;
import it.raymanrt.orient.util.Joiner;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static com.google.common.collect.Maps.newLinkedHashMap;
import static com.google.common.collect.Sets.newLinkedHashSet;
import static it.raymanrt.orient.query.Projection.projection;
import static it.raymanrt.orient.query.Target.target;
import static it.raymanrt.orient.util.Token.FETCHPLAN;
import static it.raymanrt.orient.util.Token.FROM;
import static it.raymanrt.orient.util.Token.GROUP_BY;
import static it.raymanrt.orient.util.Token.LET;
import static it.raymanrt.orient.util.Token.LIMIT;
import static it.raymanrt.orient.util.Token.LOCK;
import static it.raymanrt.orient.util.Token.ORDER_BY;
import static it.raymanrt.orient.util.Token.PARALLEL;
import static it.raymanrt.orient.util.Token.SELECT;
import static it.raymanrt.orient.util.Token.SKIP;
import static it.raymanrt.orient.util.Token.TIMEOUT;
import static it.raymanrt.orient.util.Token.WHERE;
import static java.lang.String.format;

public class Query implements Assignable {

	private Set<Projection> projections = new LinkedHashSet<Projection>();
	private boolean selectAll = false;

	private Target target = Target.DEFAULT;

	private Map<String, Assignable> letMap = newLinkedHashMap();

	private Set<Clause> clauses = newLinkedHashSet();

	private Set<Ordering> orderBy = newLinkedHashSet();

	private Optional<Projection> groupBy = Optional.absent();

	private Optional<String> limit = Optional.absent();

	private Set<String> fetchPlan = newLinkedHashSet();

	private int skipValue = 0;
	private String skipVariable = "";

	private Optional<Long> timeoutInMS = Optional.absent();
	private Optional<TimeoutStrategy> timeoutStrategy = Optional.absent();

	private Optional<LockingStrategy> lock = Optional.absent();

	private boolean isParallel = false;

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

	public Query from(Target target) {
		this.target = target;
		return this;
	}

	public Query from(String target) {
		this.target = target(target);
		return this;
	}

	public Query where(Clause clause) {
		clauses.add(clause);
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
		limit = Optional.of(Integer.toString(l));
		return this;
	}

	public Query limit(String variable) {
		limit = Optional.of(variable);
		return this;
	}

	public Query fetchPlan(FetchingStrategy strategy) {
		fetchPlan.add(strategy.toString());
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
		this.timeoutInMS = Optional.of(timeoutInMS);
		this.timeoutStrategy = Optional.of(strategy);
		return this;
	}

	public Query timeout(long timeoutInMS) {
		this.timeoutInMS = Optional.of(timeoutInMS);
		this.timeoutStrategy = Optional.of(TimeoutStrategy.EXCEPTION);
		return this;
	}

	public Query lockRecord() {
		lock = Optional.of(LockingStrategy.RECORD);
		return this;
	}

	public Query lockReset() {
		lock = Optional.absent();
		return this;
	}

	public Query lock(LockingStrategy strategy) {
		lock = Optional.of(strategy);
		return this;
	}

	public Query parallel() {
		isParallel = true;
		return this;
	}

	public Query parallel(boolean isParallel) {
		this.isParallel = isParallel;
		return this;
	}

	public String toString() {
		String base = SELECT + " %s " + FROM + " %s ";

		String joinedProjections = joinProjections();
		String query = format(base, joinedProjections, target);

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

	private String joinWhere() {
		if(clauses.size() == 1) {
			String flattenWhere = Joiner.andJoiner.join(clauses);
			return " " + WHERE + " " + flattenWhere + " ";
		}
		if(clauses.size() > 1) {
			CompositeClause and = new CompositeClause(Joiner.andJoiner, clauses.toArray(new Clause[]{}));
			return " " + WHERE + " " + and.toString() + " ";
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

	private String generateLimit() {
		if(limit.isPresent()) {
			return " " + LIMIT + " " + limit.get();
		}
		return "";

	}

	private String generateFetchPlan() {
		if(fetchPlan.size() > 0) {
			String fetchPlanString = Joiner.listJoiner.join(fetchPlan);
			return " " + FETCHPLAN + " " + fetchPlanString + " ";
		}
		return "";
	}

	private String generateTimeout() {
		String timeout = "";
		if(timeoutInMS.isPresent())
			timeout += " " + TIMEOUT + " " + timeoutInMS.get().toString() + " ";
		if(timeoutStrategy.isPresent())
			timeout += timeoutStrategy.get().toString() + " ";
		return  timeout;
	}

	private String generateLock() {
		if(lock.isPresent())
			return " " + LOCK + " " + lock.get().toString() + " ";
		return  "";
	}

	private String generateParallel() {
		if(isParallel) return " " + PARALLEL + " ";
		return "";
	}

	@Override
	public String getAssignment() {
		return toString();
	}
}