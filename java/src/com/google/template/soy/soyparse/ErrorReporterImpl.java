/*
 * Copyright 2015 Google Inc.
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

package com.google.template.soy.soyparse;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.template.soy.base.SoySyntaxException;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple {@link ErrorReporter} implementation.
 *
 * @author brndn@google.com (Brendan Linn)
 */
class ErrorReporterImpl implements ErrorReporter {
  protected final List<SoySyntaxException> errors = new ArrayList<>();

  @Override
  public void report(SoySyntaxException e) {
    errors.add(e);
  }

  @Override
  public Checkpoint checkpoint() {
    return new Checkpoint(errors.size());
  }

  @Override
  public boolean errorsSince(Checkpoint checkpoint) {
    return errors.size() > checkpoint.numErrors;
  }

  /**
   * Returns the full list of errors reported to this error manager.
   * This is a package-private implementation method, rather than a public interface method,
   * because there is only one caller that needs to know the full list of errors:
   * {@link SoyFileSetParser#parseWithVersions}.
   */
  ImmutableCollection<? extends SoySyntaxException> getErrors() {
    return ImmutableList.copyOf(errors);
  }
}
