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

package com.github.raymanrt.orientqb.util;

public class Joiner {

    public final static com.google.common.base.Joiner listJoiner = com.google.common.base.Joiner.on(", ");
    public final static com.google.common.base.Joiner commaJoiner = com.google.common.base.Joiner.on(",");
    public final static com.google.common.base.Joiner oneSpaceJoiner = com.google.common.base.Joiner.on(" ");
    public final static com.google.common.base.Joiner andJoiner = com.google.common.base.Joiner.on(" " + Token.AND + " ");
    public final static com.google.common.base.Joiner orJoiner = com.google.common.base.Joiner.on(" " + Token.OR + " ");

}
