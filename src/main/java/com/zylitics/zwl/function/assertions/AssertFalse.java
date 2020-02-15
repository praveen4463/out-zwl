package com.zylitics.zwl.function.assertions;

import javax.annotation.Nullable;

public class AssertFalse extends Assert {
  
  @Override
  public String getName() {
    return "assertFalse";
  }
  
  @Override
  protected void assertCondition(boolean condition, @Nullable String message) {
    // just invert the condition and invoke.
    super.assertCondition(!condition, message);
  }
}
