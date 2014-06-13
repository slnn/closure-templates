/*
 * Copyright 2011 Google Inc.
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

package com.google.template.soy.soytree;

import com.google.common.collect.ImmutableMap;

import java.util.Set;


/**
 * Specifies how the outputs of <code>{print}</code> commands that lack escaping directives are
 * encoded.
 *
 */
public enum AutoescapeMode {

  /** Auto-escaping is off for the template. */
  FALSE("deprecated-noautoescape", "false"),
  /** Auto-escaping is on for the template so directiveless prints will be HTML escaped. */
  TRUE("deprecated-noncontextual", "true"),
  /**
   * Contextual auto-escaping is on for the template so directiveless prints will be escaped based
   * on the surrounding context.
   */
  CONTEXTUAL("deprecated-contextual", "contextual"),
  /**
   * Strict form of contextual autoescaping in which no autoescape-cancelling print directives nor
   * calls to non-strict templates are allowed.
   */
  STRICT("strict", null),
  ;

  private static final ImmutableMap<String, AutoescapeMode> valueToModeMap;

  static {
    ImmutableMap.Builder <String, AutoescapeMode> map = ImmutableMap.builder();
    for (AutoescapeMode value : AutoescapeMode.values()) {
      map.put(value.attributeValue, value);
      if (value.synomym != null) {
        map.put(value.synomym, value);
      }
    }
    valueToModeMap = map.build();
  }


  private final String attributeValue;
  private final String synomym;


  /**
   * Constructs an AutoescapeMode enum.
   *
   * @param attributeValue value of the "autoescape" attribute that specifies this autoescape mode.
   * @param synonym an (optional) synonymous attribute value, for backwards compatibility.
   */
  AutoescapeMode(String attributeValue, String synonym) {
    this.attributeValue = attributeValue;
    this.synomym = synonym;
  }


  /**
   * Returns a form of this attribute's name suitable for use in a template attribute.
   */
  public String getAttributeValue() {
    return attributeValue;
  }


  /**
   * The set created by element-wise application of {@link #getAttributeValue} to all modes.
   */
  public static Set<String> getAttributeValues() {
    return valueToModeMap.keySet();
  }


  /**
   * The value such that attributeValue.equals(value.getAttributeValue()).
   */
  public static AutoescapeMode forAttributeValue(String attributeValue) {
    return valueToModeMap.get(attributeValue);
  }
}
