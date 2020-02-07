package com.zylitics.zwl.function.assertions;

import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.exception.AssertionFailedException;
import com.zylitics.zwl.function.AbstractFunction;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

/**
 * Assert takes an expression and expects it to be a condition of type {@link Boolean}. If the
 * condition evaluates to {@code false}, an {@link AssertionFailedException} is thrown. It doesn't
 * return anything.
 */
public class Assert extends AbstractFunction {
  
  @Override
  public String getName() {
    return "assert";
  }
  
  @Override
  public int minParamsCount() {
    return 1;
  }
  
  @Override
  public int maxParamsCount() {
    return 2;
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    assertArgs(args);
    int argsCount = args.size();
    
    switch (argsCount) {
      case 1:
        assertTrue(tryCastBoolean(0, args.get(0)));
        break;
      case 2:
        // If some parameter is optional (Nullable) and we get a Nothing for that, send null there
        // so that we don't throw cast exception on optional parameters that could be ignored.
        ZwlValue firstArg = args.get(1);
        assertTrue(
            tryCastBoolean(0, args.get(0)),
            firstArg.getNothingValue().isPresent() ? null : tryCastString(1, firstArg)
        );
        break;
      default:
        throw unexpectedEndOfFunctionOverload(argsCount);
    }
    return _void;
  }
  
  private void assertTrue(boolean condition) {
    assertTrue(condition, null);
  }
  
  private void assertTrue(boolean condition, @Nullable String message) {
    if (!condition) {
      throw new AssertionFailedException(message != null ? message : "Assertion was failed.");
    }
  }
}
