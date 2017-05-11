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

package com.github.raymanrt.orientqb.query.core;

import com.github.raymanrt.orientqb.query.Clause;
import com.github.raymanrt.orientqb.query.LockingStrategy;
import com.github.raymanrt.orientqb.query.Target;
import com.github.raymanrt.orientqb.query.TimeoutStrategy;
import com.github.raymanrt.orientqb.query.clause.CompositeClause;
import com.google.common.base.Optional;

import java.util.Set;

import static com.github.raymanrt.orientqb.query.Target.target;
import static com.github.raymanrt.orientqb.util.Joiner.andJoiner;
import static com.github.raymanrt.orientqb.util.Token.LIMIT;
import static com.github.raymanrt.orientqb.util.Token.LOCK;
import static com.github.raymanrt.orientqb.util.Token.PARALLEL;
import static com.github.raymanrt.orientqb.util.Token.TIMEOUT;
import static com.github.raymanrt.orientqb.util.Token.WHERE;
import static com.google.common.collect.Sets.newLinkedHashSet;

/**
 * Created by rayman on 28/04/16.
 */
public abstract class AbstractQuery {

    private Target target = Target.DEFAULT;

    private Set<Clause> clauses = newLinkedHashSet();

    private Optional<LockingStrategy> lock = Optional.absent();

    private boolean isParallel = false; // TODO: bring to Query?

    private Optional<String> limit = Optional.absent();

    private Optional<Long> timeoutInMS = Optional.absent();
    private Optional<TimeoutStrategy> timeoutStrategy = Optional.absent();

    public Target getTarget() {
        return target;
    }

    protected AbstractQuery fromEmpty() {
        this.target = Target.EMPTY;
        return this;
    }

    public AbstractQuery setTarget(Target target) {
        this.target = target;
        return this;
    }

    public AbstractQuery setTarget(String target) {
        this.target = target(target);
        return this;
    }

    public AbstractQuery where(Clause clause) {
        if(!clause.isEmpty()) {
            clauses.add(clause);
        }
        return this;
    }

    protected AbstractQuery lock(LockingStrategy strategy) {
        lock = Optional.of(strategy);
        return this;
    }

    protected AbstractQuery lockReset() {
        lock = Optional.absent();
        return this;
    }

    protected AbstractQuery parallel() {
        isParallel = true;
        return this;
    }

    protected AbstractQuery parallel(boolean parallel) {
        isParallel = parallel;
        return this;
    }

    public String joinWhere() {
        if(clauses.size() == 1) {
            String flattenWhere = andJoiner.join(clauses);
            return new StringBuilder(" ")
                    .append(WHERE)
                    .append(" ")
                    .append(flattenWhere)
                    .append(" ")
                    .toString();
        }
        if(clauses.size() > 1) {
            CompositeClause and = new CompositeClause(andJoiner, clauses.toArray(new Clause[]{}));
            return new StringBuilder(" ")
                    .append(WHERE)
                    .append(" ")
                    .append(and)
                    .append(" ")
                    .toString();
        }
        return "";
    }

    protected String generateLock() {
        if(lock.isPresent()) {
            return new StringBuilder(" ")
                    .append(LOCK)
                    .append(" ")
                    .append(lock.get())
                    .append(" ")
                    .toString();
        }
        return  "";
    }

    protected String generateParallel() {
        if(isParallel) {
            return new StringBuilder(" ")
                    .append(PARALLEL)
                    .append(" ")
                    .toString();
        }
        return "";
    }

    protected AbstractQuery limit(int l) {
        limit = Optional.of(Integer.toString(l));
        return this;
    }

    protected AbstractQuery limit(String variable) {
        limit = Optional.of(variable);
        return this;
    }

    protected String generateLimit() {
        if(limit.isPresent()) {
            return new StringBuilder(" ")
                    .append(LIMIT)
                    .append(" ")
                    .append(limit.get())
                    .toString();
        }
        return "";

    }

    protected AbstractQuery timeout(long timeoutInMS, TimeoutStrategy strategy) {
        this.timeoutInMS = Optional.of(timeoutInMS);
        this.timeoutStrategy = Optional.of(strategy);
        return this;
    }

    protected AbstractQuery timeout(long timeoutInMS) {
        this.timeoutInMS = Optional.of(timeoutInMS);
        return this;
    }

    protected String generateTimeout() {
        StringBuilder timeout = new StringBuilder("");
        if(timeoutInMS.isPresent()) {
            timeout.append(" ")
                    .append(TIMEOUT)
                    .append(" ")
                    .append(timeoutInMS.get())
                    .append(" ");
        }
        if(timeoutStrategy.isPresent()) {
            timeout.append(timeoutStrategy.get())
                    .append(" ");
        }
        return  timeout.toString();
    }
}
