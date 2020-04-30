package com.zylitics.zwl.api;

import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.interpret.Function;

import java.util.List;
import java.util.Set;

public interface ZwlInterpreter {
  
  void setReadOnlyVariable(String identifier, ZwlValue value);
  
  @SuppressWarnings("unused")
  void setFunctions(Set<Function> functions);
  
  void setFunction(Function function);
  
  void setLineChangeListener(InterpreterLineChangeListener lineChangeListener);
  
  @SuppressWarnings("unused")
  void setLineChangeListeners(List<InterpreterLineChangeListener> lineChangeListeners);
}
