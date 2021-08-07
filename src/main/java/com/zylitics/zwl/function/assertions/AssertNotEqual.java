package com.zylitics.zwl.function.assertions;

public class AssertNotEqual extends AbstractAssertEquality {
  
  @Override
  public String getName() {
    return "assertNotEqual";
  }
  
  @Override
  protected boolean eqOp() {
    return false;
  }
}