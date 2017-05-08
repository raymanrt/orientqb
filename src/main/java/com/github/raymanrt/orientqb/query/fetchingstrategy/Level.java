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

package com.github.raymanrt.orientqb.query.fetchingstrategy;

/**
 * Created by rayman on 25/05/16.
 */
public class Level {

    private Level() {};

    private static class AtomicLevel extends Level {
        private final int level;

        public AtomicLevel(int level) {
            this.level = level;
        }

        public String toString() {
            return "[" + level + "]";
        }
    }

    private static class RangeLevel extends Level {

        private final String fromLevel;
        private final String toLevel;

        public RangeLevel(Integer fromLevel, Integer toLevel) {
            this.fromLevel = fromLevel == null ? "" : Integer.toString(fromLevel);
            this.toLevel = toLevel == null ? "" : Integer.toString(toLevel);
        }

        public String toString() {
            return "[" + fromLevel + "-" + toLevel + "]";
        }
    }

    public static Level level(int level) {
        return new AtomicLevel(level);
    }

    public static Level fromLevel(int fromLevel) {
        return new RangeLevel(fromLevel, null);
    }

    public static Level toLevel(int toLevel) {
        return new RangeLevel(null, toLevel);
    }

    public static Level level(int fromLevel, int toLevel) {
        return new RangeLevel(fromLevel, toLevel);
    }

    private static class AnyLevel extends Level {
        public String toString() {
            return "[*]";
        }
    }

    public static Level anyLevel() {
        return new AnyLevel();
    }
}
