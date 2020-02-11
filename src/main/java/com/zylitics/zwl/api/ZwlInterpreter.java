package com.zylitics.zwl.api;

import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.interpret.DefaultZwlInterpreter;
import com.zylitics.zwl.interpret.Function;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;

public interface ZwlInterpreter {
  
  void setReadOnlyVariable(String identifier, ZwlValue value);
  
  void setFunctions(List<Function> functions);
  
  void setFunction(Function function);
}
