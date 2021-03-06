/*
 * Copyright 2018 Google Inc.
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

package com.google.template.soy.basicfunctions;

import com.google.common.collect.ImmutableSet;
import com.google.template.soy.plugin.restricted.SoySourceFunction;

/** Lists all functions in this package. */
public class BasicFunctions {

  private BasicFunctions() {}

  public static ImmutableSet<SoySourceFunction> functions() {
    return ImmutableSet.of(
        new AugmentMapFunction(),
        new CeilingFunction(),
        new ConcatListsFunction(),
        new FloorFunction(),
        new IsNonnullFunction(),
        new IsNullFunction(),
        new LengthFunction(),
        new ListContainsFunction(),
        new MaxFunction(),
        new MinFunction(),
        new ParseFloatFunction(),
        new ParseIntFunction(),
        new RangeFunction(),
        new RandomIntFunction(),
        new RoundFunction(),
        new SqrtFunction());
  }
}
