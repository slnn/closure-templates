/*
 * Copyright 2017 Google Inc.
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

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.template.soy.jbcsrc.restricted.BytecodeUtils;
import com.google.template.soy.jbcsrc.restricted.Expression;
import com.google.template.soy.jbcsrc.restricted.JbcSrcPluginContext;
import com.google.template.soy.jbcsrc.restricted.MethodRef;
import com.google.template.soy.jbcsrc.restricted.SoyExpression;
import com.google.template.soy.jbcsrc.restricted.SoyJbcSrcFunction;
import com.google.template.soy.jssrc.restricted.JsExpr;
import com.google.template.soy.jssrc.restricted.SoyLibraryAssistedJsSrcFunction;
import com.google.template.soy.plugin.java.restricted.JavaPluginContext;
import com.google.template.soy.plugin.java.restricted.JavaValue;
import com.google.template.soy.plugin.java.restricted.JavaValueFactory;
import com.google.template.soy.plugin.java.restricted.SoyJavaSourceFunction;
import com.google.template.soy.pysrc.restricted.PyExpr;
import com.google.template.soy.pysrc.restricted.PyFunctionExprBuilder;
import com.google.template.soy.pysrc.restricted.SoyPySrcFunction;
import com.google.template.soy.shared.restricted.Signature;
import com.google.template.soy.shared.restricted.SoyFunctionSignature;
import com.google.template.soy.shared.restricted.TypedSoyFunction;
import com.google.template.soy.types.IntType;
import com.google.template.soy.types.ListType;
import java.lang.reflect.Method;
import java.util.List;
import org.objectweb.asm.Type;

/**
 * The Range function takes 1-3 arguments and generates a sequence of integers, like the python
 * {@code range} function.
 *
 * <ul>
 *   <li>The first argument is the end of the range if it is the only argument (in which case the
 *       start is assumed to be 0), otherwise it is the start of the range
 *   <li>The second argument is the end of the range.
 *   <li>The third argument is the 'step', which defaults to 1.
 * </ul>
 */
@SoyFunctionSignature(
    name = "range",
    value = {
      @Signature(
          parameterTypes = {"number"},
          returnType = "list<int>"),
      @Signature(
          parameterTypes = {"number", "number"},
          returnType = "list<int>"),
      @Signature(
          parameterTypes = {"number", "number", "number"},
          returnType = "list<int>")
    })
public final class RangeFunction extends TypedSoyFunction
    implements SoyJavaSourceFunction,
        SoyLibraryAssistedJsSrcFunction,
        SoyPySrcFunction,
        SoyJbcSrcFunction {

  private static final class Methods {
    static final Method RANGE_1 =
        JavaValueFactory.createMethod(BasicFunctionsRuntime.class, "range", int.class);
    static final MethodRef RANGE_1_REF = MethodRef.create(RANGE_1).asNonNullable();

    static final Method RANGE_2 =
        JavaValueFactory.createMethod(BasicFunctionsRuntime.class, "range", int.class, int.class);
    static final MethodRef RANGE_2_REF = MethodRef.create(RANGE_2).asNonNullable();

    static final Method RANGE_3 =
        JavaValueFactory.createMethod(
            BasicFunctionsRuntime.class, "range", int.class, int.class, int.class);
    static final MethodRef RANGE_3_REF = MethodRef.create(RANGE_3).asNonNullable();
  }

  @Override
  public JavaValue applyForJavaSource(
      JavaValueFactory factory, List<JavaValue> args, JavaPluginContext context) {
    switch (args.size()) {
      case 1:
        return factory.callStaticMethod(Methods.RANGE_1, args.get(0));
      case 2:
        return factory.callStaticMethod(Methods.RANGE_2, args.get(0), args.get(1));
      case 3:
        return factory.callStaticMethod(Methods.RANGE_3, args.get(0), args.get(1), args.get(2));
      default:
        throw new AssertionError();
    }
  }

  @Override
  public SoyExpression computeForJbcSrc(JbcSrcPluginContext context, List<SoyExpression> args) {
    ListType intList = ListType.of(IntType.getInstance());
    switch (args.size()) {
      case 1:
        return SoyExpression.forList(intList, Methods.RANGE_1_REF.invoke(asInt(args.get(0))));
      case 2:
        return SoyExpression.forList(
            intList, Methods.RANGE_2_REF.invoke(asInt(args.get(0)), asInt(args.get(1))));
      case 3:
        return SoyExpression.forList(
            intList,
            Methods.RANGE_3_REF.invoke(asInt(args.get(0)), asInt(args.get(1)), asInt(args.get(2))));
      default:
        throw new AssertionError();
    }
  }

  private static Expression asInt(SoyExpression soyExpression) {
    return BytecodeUtils.numericConversion(soyExpression.unboxAs(long.class), Type.INT_TYPE);
  }

  @Override
  public PyExpr computeForPySrc(List<PyExpr> args) {
    // Coincidentally, soy range is identical to python xrange
    // in theory we should use range which is guaranteed to produce a list.  But the xrange object
    // is also enumerable, so as far as soy is concerned it is also a list and we can just use it.
    return new PyFunctionExprBuilder("xrange").addArgs(args).asPyExpr();
  }

  @Override
  public JsExpr computeForJsSrc(List<JsExpr> args) {
    return new JsExpr(
        "goog.array.range("
            + Joiner.on(", ")
                .join(
                    Iterables.transform(
                        args,
                        new Function<JsExpr, String>() {

                          @Override
                          public String apply(JsExpr input) {
                            return input.getText();
                          }
                        }))
            + ")",
        Integer.MAX_VALUE);
  }

  @Override
  public ImmutableSet<String> getRequiredJsLibNames() {
    return ImmutableSet.of("goog.array");
  }
}
