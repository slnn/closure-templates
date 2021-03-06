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

package com.google.template.soy.plugin.java.restricted;

import java.lang.reflect.Method;
import java.util.List;

/**
 * A factory for instructing soy how to implement a {@link SoyJavaSourceFunction} or {@link
 * SoyJavaSourcePrintDirective} at runtime.
 */
public abstract class JavaValueFactory {

  /** Instructs Soy to call the given static {@code method} with the given params at runtime. */
  public abstract JavaValue callStaticMethod(Method method, JavaValue... params);

  /**
   * Instructs Soy to call the given {@code method} with the given params at runtime. The class the
   * runtime is within must be registered with Soy as the {@link JavaPluginRuntime} for this plugin.
   */
  public abstract JavaValue callRuntimeMethod(Method method, JavaValue... params);

  /**
   * Returns a JavaValue that corresponds to a list containing each of the values. The values will
   * be wrapped in {@code SoyValue} instances if they are not already SoyValues.
   */
  public abstract JavaValue listOf(List<JavaValue> args);

  /**
   * Convenience method for retrieving a {@link Method} from a class.
   *
   * @see Class#getMethod(String, Class...)
   */
  public static Method createMethod(Class<?> clazz, String methodName, Class<?>... params) {
    try {
      return clazz.getMethod(methodName, params);
    } catch (NoSuchMethodException | SecurityException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
