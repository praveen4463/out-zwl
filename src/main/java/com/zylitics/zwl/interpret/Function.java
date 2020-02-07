package com.zylitics.zwl.interpret;

import com.zylitics.zwl.datatype.ZwlValue;

import java.util.List;
import java.util.function.Supplier;

public interface Function {
  
  String getName();
  
  int minParamsCount();
  
  int maxParamsCount();
  
  ZwlValue invoke(List<ZwlValue> args,
                  Supplier<ZwlValue> defaultValue,
                  Supplier<String> lineNColumn);
}
