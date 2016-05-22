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

import com.github.raymanrt.orientqb.query.LockingStrategy;
import org.junit.Test;

import static com.github.raymanrt.orientqb.query.Projection.projection;
import static com.github.raymanrt.orientqb.query.Target.cluster;
import static com.github.raymanrt.orientqb.query.Target.index;
import static org.junit.Assert.assertEquals;

/**
 * Created by rayman on 28/04/16.
 */
public class DeleteTest {

    @Test
    public void deleteTest() {
        Delete delete = new Delete()
                .from("Profile")
                .where(projection("surname").toLowerCase().eq("unknown"));

        assertEquals("DELETE Profile WHERE surname.toLowerCase() = 'unknown'", delete.toString());

    }

    @Test
    public void deleteFromClusterTest() {
        Delete delete = new Delete()
                .from(cluster(5))
                .where(projection("surname").toLowerCase().eq("unknown"));

        assertEquals("DELETE cluster:5 WHERE surname.toLowerCase() = 'unknown'", delete.toString());

    }

    @Test
    public void deleteFromIndexTest() {
        Delete delete = new Delete()
                .from(index("profile"))
                .where(projection("surname").toLowerCase().eq("unknown"));

        assertEquals("DELETE index:profile WHERE surname.toLowerCase() = 'unknown'", delete.toString());

    }

    @Test
    public void lockTest() {
        Delete delete = new Delete()
                .from("Profile")
                .where(projection("surname").toLowerCase().eq("unknown"))
                .lock(LockingStrategy.RECORD);

        assertEquals("DELETE Profile LOCK RECORD WHERE surname.toLowerCase() = 'unknown'", delete.toString());

    }

    @Test
    public void returnTest() {
        Delete delete = new Delete()
                .from("Profile")
                .where(projection("surname").toLowerCase().eq("unknown"))
                .returnStrategy(ReturnStrategy.BEFORE);

        assertEquals("DELETE Profile RETURN BEFORE WHERE surname.toLowerCase() = 'unknown'", delete.toString());

        delete.returnReset();

        assertEquals("DELETE Profile WHERE surname.toLowerCase() = 'unknown'", delete.toString());

    }

    @Test
    public void limitTest() {
        Delete delete = new Delete()
                .from("Profile")
                .where(projection("surname").toLowerCase().eq("unknown"))
                .limit(5);

        assertEquals("DELETE Profile WHERE surname.toLowerCase() = 'unknown' LIMIT 5", delete.toString());

    }

    @Test
    public void timeoutTest() {
        Delete delete = new Delete()
                .from("Profile")
                .where(projection("surname").toLowerCase().eq("unknown"))
                .timeout(5);

        assertEquals("DELETE Profile WHERE surname.toLowerCase() = 'unknown' TIMEOUT 5", delete.toString());

    }

}