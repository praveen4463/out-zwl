package com.zylitics.zwl.function.assertions;

import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.exception.AssertionFailedException;
import com.zylitics.zwl.exception.InvalidTypeException;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.function.Supplier;

public abstract class AbstractAssertEquality extends AbstractFunction {
  
  @Override
  public int minParamsCount() {
    return 2;
  }
  
  @Override
  public int maxParamsCount() {
    return 3;
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
    int argsCount = args.size();
    
    if (argsCount < 2) {
      throw unexpectedEndOfFunctionOverload(argsCount);
    }
  
    ZwlValue expected = args.get(0);
    ZwlValue actual = args.get(1);
    String message = null;
    if (argsCount == 3) {
      message = args.get(2).getNothingValue().isPresent() ? null : tryCastString(2, args.get(2));
    }
    if (!validate(expected, actual)) {
      throw new AssertionFailedException(fromPos.get(), toPos.get(),
          withLineNCol(message != null
              ? message
              : String.format("Assertion was failed, expected %s to be %s to %s",
              expected,
              eqOp() ? "equal" : "not equal",
              actual)));
    }
    
    return _void;
  }
  
  protected abstract boolean eqOp();
  
  @SuppressWarnings("OptionalGetWithoutIsPresent")
  private boolean validate(ZwlValue expected, ZwlValue actual) {
    if (expected.getClass() == actual.getClass()) {
      return eqOp() == expected.equals(actual);
    }
  
    switch (expected.getType()) {
      case Types.STRING:
        return eqOp() == expected.toString().equals(actual.toString());
      case Types.BOOLEAN:
        return eqOp() == expected.getBooleanValue().get().equals(parseBoolean(1, actual));
      case Types.NUMBER:
        return eqOp() == expected.getDoubleValue().get().equals(parseDouble(1, actual));
      default:
        throw new InvalidTypeException(fromPos.get(),
            toPos.get(),
            withLineNCol(String.format("Can't convert type %s to %s and vice-versa",
                actual.getType(),
                expected.getType()))
        );
    }
  }
}
