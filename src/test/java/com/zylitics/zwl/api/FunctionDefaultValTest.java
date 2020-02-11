package com.zylitics.zwl.api;

import com.zylitics.zwl.datatype.ZwlValue;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Supplier;

public class FunctionDefaultValTest {

  @Test
  void defaultValueTest() {
    String code = String.join(System.getProperty("line.separator"),
        "a = \"Hi there\"",
        "a = lower(a)??{\"a_default_value_instead\"}",
        "assert(a == \"a_default_value_instead\")");
    
    Main main = new Main(code);
    main.interpretDevOnly(zwlInterpreter -> zwlInterpreter.setFunction(new Lower()));
  }
  
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
