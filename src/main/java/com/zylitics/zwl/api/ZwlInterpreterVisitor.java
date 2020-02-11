package com.zylitics.zwl.api;

@FunctionalInterface
public interface ZwlInterpreterVisitor {
  
  void visit(ZwlInterpreter zwlInterpreter);
}
