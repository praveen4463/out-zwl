package com.zylitics.zwl.api;

import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.util.StringUtil;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Supplier;

/**
 * Tests how function's default value work and how externally added function can overwrite existing
 * one if they match.
 */
public class FunctionDefaultValTest {

  @Test
  void defaultValueTest() {
    String code = String.join(StringUtil.getPlatformLineSeparator(),
        "a = \"Hi there\"",
        "a = lower(a)??{\"a_default_value_instead\"}",
        "assert(a == \"a_default_value_instead\")");
    
    Main main = new Main(code, AllTests.DEFAULT_TEST_LISTENERS);
    // set our overridden function that should overwrite the existing one.
    main.interpretDevOnly(zwlInterpreter -> zwlInterpreter.setFunction(new Lower()));
  }
  
  /**
   * Shows how functions can be added externally to extend ZWL functionality. Functions can support
   * any functionality as long as they adhere to {@link com.zylitics.zwl.interpret.Function}. Added
   * functions should preferably extend {@link com.zylitics.zwl.function.AbstractFunction} for
   * convenience.
   */
  public static class Lower extends com.zylitics.zwl.function.string.Lower {
  
    @Override
    public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                           Supplier<String> lineNColumn) {
      if (defaultValue.get().getNothingValue().isPresent()) {
        return super.invoke(args, defaultValue, lineNColumn);
      }
      return defaultValue.get();
    }
  }
}
