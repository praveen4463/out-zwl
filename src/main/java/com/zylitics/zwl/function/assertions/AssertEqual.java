package com.zylitics.zwl.function.assertions;

public class AssertEqual extends AbstractAssertEquality {
  
  @Override
  public String getName() {
    return "assertEqual";
  }
  
  @Override
  protected boolean eqOp() {
    return true;
  }
}
