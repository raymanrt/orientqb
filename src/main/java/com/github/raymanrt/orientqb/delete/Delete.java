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

package com.github.raymanrt.orientqb.delete;

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

    private enum DeleteType {
        NONE,
        VERTEX,
        EDGE
    }

    private Optional<ReturnStrategy> returning = Optional.absent();
    private DeleteType type = DeleteType.NONE;


    @Override
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

    @Override
    public Delete where(Clause clause) {
        super.where(clause);
        return this;
    }


    public String toString() {
        String query = new StringBuilder(DELETE)
                .append(" ")
                .append(getType())
                .append(" ")
                .append(getTarget())
                .append(" ")

                .append(generateLock())
                .append(generateReturning())

                .append(joinWhere())
                .append(generateLimit())

                .append(generateTimeout())

                .toString();

        return Commons.clean(query);
    }

    private String getType() {
        if(type.equals(DeleteType.NONE)) {
            return "";
        }
        return new StringBuilder(" ")
                .append(type.toString())
                .append(" ")
                .toString();
    }

    private String generateReturning() {
        if(returning.isPresent())
            return new StringBuilder(" ")
                    .append(RETURN)
                    .append(" ")
                    .append(returning.get())
                    .append(" ")
                    .toString();
        return  "";
    }

    public Delete lockRecord() {
        super.lock(LockingStrategy.RECORD);
        return this;
    }

    @Override
    public Delete lockReset() {
        super.lockReset();
        return this;
    }

    @Override
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

    @Override
    public Delete limit(int l) {
        super.limit(l);
        return this;
    }

    @Override
    public Delete limit(String variable) {
        super.limit(variable);
        return this;
    }

    @Override
    public Delete timeout(long timeoutInMS, TimeoutStrategy strategy) {
        super.timeout(timeoutInMS, strategy);
        return this;
    }

    @Override
    public Delete timeout(long timeoutInMS) {
        super.timeout(timeoutInMS);
        return this;
    }

    public Delete vertex() {
        type = DeleteType.VERTEX;
        return this;
    }

    public Delete edge() {
        type = DeleteType.EDGE;
        return this;
    }
    
    @Override
    public Delete clone(){
    	Delete clone = (Delete) super.clone();
    	return clone;
    }
}
