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

package com.gitub.raymanrt.orientqb.delete;

import com.github.raymanrt.orientqb.query.Clause;
import com.github.raymanrt.orientqb.query.LockingStrategy;
import com.github.raymanrt.orientqb.query.Target;
import com.github.raymanrt.orientqb.query.TimeoutStrategy;
import com.github.raymanrt.orientqb.query.core.AbstractQuery;
import com.github.raymanrt.orientqb.util.Commons;
import com.google.common.base.Optional;

import static com.github.raymanrt.orientqb.util.Token.DELETE;
import static com.github.raymanrt.orientqb.util.Token.RETURN;

/**
 * Created by rayman on 28/04/16.
 */
public class Delete extends AbstractQuery {

    public static final String INDEX = "index:";

    private Optional<ReturnStrategy> returning = Optional.absent();


    public Delete fromEmpty() {
        super.fromEmpty();
        return this;
    }

    public Delete from(Target target) {
        super.setTarget(target);
        return this;
    }

    public Delete from(String target) {
        super.setTarget(target);
        return this;
    }

    public Delete where(Clause clause) {
        super.where(clause);
        return this;
    }


    public String toString() {
        String query = DELETE + " " + getTarget() + " ";

        query += generateLock();
        query += generateReturning();

        query += joinWhere();
        query += generateLimit();

        query += generateTimeout();

        return Commons.clean(query);
    }

    private String generateReturning() {
        if(returning.isPresent())
            return " " + RETURN + " " + returning.get().toString() + " ";
        return  "";
    }

    public Delete lockRecord() {
        super.lock(LockingStrategy.RECORD);
        return this;
    }

    public Delete lockReset() {
        super.lockReset();
        return this;
    }

    public Delete lock(LockingStrategy strategy) {
        super.lock(strategy);
        return this;
    }

    public Delete returnStrategy(ReturnStrategy returning) {
        this.returning = Optional.of(returning);
        return this;
    }

    public Delete returnReset() {
        this.returning = Optional.absent();
        return this;
    }

    public Delete limit(int l) {
        super.limit(l);
        return this;
    }

    public Delete limit(String variable) {
        super.limit(variable);
        return this;
    }

    public Delete timeout(long timeoutInMS, TimeoutStrategy strategy) {
        super.timeout(timeoutInMS, strategy);
        return this;
    }

    public Delete timeout(long timeoutInMS) {
        super.timeout(timeoutInMS);
        return this;
    }
}
