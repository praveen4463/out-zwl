package com.zylitics.zwl.function.debugging;

import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.function.Supplier;

// should write into a stream that is accessible from outside, once read the read content is deleted
// to free up space. when dry running, we will read in the end and when running on runner, we can
// read in time splits.

/**
 * Prints given message and a line break on the console. To print just a line break, send empty
 * string.
 */
public class Print extends AbstractFunction {
  
  @Override
  public String getName() {
    return "print";
  }
  
  @Override
  public int minParamsCount() {
    return 1;
  }
  
  @Override
  public int maxParamsCount() {
    return 1;
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
    int argsCount = args.size();
    
    if (argsCount == 1) {
      print(args.get(0).toString());
      return _void;
    }
  
    throw unexpectedEndOfFunctionOverload(argsCount);
  }
  
  private void print(String message) {
    // The default implementation writes to std out, other implementations may write to different
    // streams.
    System.out.println(message);
  }
}
