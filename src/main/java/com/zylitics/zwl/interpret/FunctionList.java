package com.zylitics.zwl.interpret;

import com.google.common.collect.ImmutableList;
import com.zylitics.zwl.function.assertions.Assert;
import com.zylitics.zwl.function.util.Exists;
import com.zylitics.zwl.function.debugging.Print;

import java.util.List;

public final class FunctionList {
  
  private FunctionList() {}
  
  public static List<Function> get() {
    ImmutableList.Builder<Function> builder = ImmutableList.builder();
    builder.add(
        new Assert(),
        new Exists(),
        new Print()
    );
    return builder.build();
  }
}
