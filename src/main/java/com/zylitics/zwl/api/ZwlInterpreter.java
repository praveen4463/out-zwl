package com.zylitics.zwl.api;

import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.interpret.Function;

import java.util.List;
import java.util.Set;

public interface ZwlInterpreter {
  
  /**
   * Adds a readonly variable to the set of readonly variables for this interpreter. If a matching
   * variable already exists (by name), this method will return.
   * @param identifier The variable identifier to be added
   * @param value The value of variable to be added
   */
  void addReadOnlyVariable(String identifier, ZwlValue value);
  
  /**
   * Adds {@link Set} of {@link Function}s to the {@link Set} of {@link Function}s for this
   * interpreter. Any {@link Function} already present in interpreter won't be added.
   * @param functions {@link Set} of {@link Function}s to be added into the interpreter's Set of
   *                  functions
   */
  void addFunctions(Set<Function> functions);
  
  /**
   * Adds a {@link Function} to the {@link Set} of {@link Function}s for this interpreter. If the
   * function being added already present in the interpreter, this method will return.
   * @param function A {@link Function} to be added into the interpreter's Set of functions
   */
  void addFunction(Function function);
  
  /**
   * Sets a line change listener to the interpreter that will be invoked every time interpreter
   * moves to next line in the given code.
   * @param lineChangeListener An instance of {@link InterpreterLineChangeListener} to be set as
   *                           interpreter's
   */
  void setLineChangeListener(InterpreterLineChangeListener lineChangeListener);
  
  @SuppressWarnings("unused")
  void setLineChangeListeners(List<InterpreterLineChangeListener> lineChangeListeners);
}
