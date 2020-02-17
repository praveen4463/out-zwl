package com.zylitics.zwl.api;

import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.interpret.Function;

import java.util.List;

public interface ZwlInterpreter {
  
  void setReadOnlyVariable(String identifier, ZwlValue value);
  
  @SuppressWarnings("unused")
  void setFunctions(List<Function> functions);
  
  void setFunction(Function function);
}
