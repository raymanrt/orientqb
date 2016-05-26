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

/**
 * Created by rayman on 25/05/16.
 */
public enum JsonFormat {

    TYPE("type"),
    RID("rid"),
    VERSION("version"),
    CLASS("class"),
    ATTRIB_SAME_ROW("attibSameRow"),
    INDENT("indent"),
    ALWAYS_FETCH_EMBEDDED("alwaysFetchEmbedded"),
    DATE_AS_LONG("dateAsLong"),
    PRETTY_PRINT("prettyPrint");

    private final String format;

    private JsonFormat(String format) {
        this.format = format;
    }

    public String toString() {
        return format;
    }
}
