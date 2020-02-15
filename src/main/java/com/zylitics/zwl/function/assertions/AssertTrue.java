package com.zylitics.zwl.function.assertions;

/**
 * AssertTrue is same as {@link Assert}, both can be used interchangeably.
 */
public class AssertTrue extends Assert {
  
  @Override
  public String getName() {
    return "assertTrue";
  }
}
