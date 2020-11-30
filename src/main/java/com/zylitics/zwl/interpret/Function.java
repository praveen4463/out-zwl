package com.zylitics.zwl.interpret;

import com.zylitics.zwl.datatype.ZwlValue;

import java.util.List;
import java.util.function.Supplier;

// Note: Function objects shouldn't keep anything in state that is not going to update in each
// call, means each call should contains relevant state because functions are instantiated once
// per execution and if a function is called more than once, state must remain exclusive to a call.
public interface Function {
  
  String getName();
  
  int minParamsCount();
  
  int maxParamsCount();
  
  Function setFromPos(Supplier<String> fromPos);
  
  Function setToPos(Supplier<String> toPos);
  
  ZwlValue invoke(List<ZwlValue> args,
                  Supplier<ZwlValue> defaultValue,
                  Supplier<String> lineNColumn);
}
