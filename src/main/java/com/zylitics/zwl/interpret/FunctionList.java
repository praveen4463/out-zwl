package com.zylitics.zwl.interpret;

import com.zylitics.zwl.function.assertions.Assert;
import com.zylitics.zwl.function.util.Exists;
import com.zylitics.zwl.function.debugging.Print;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class FunctionList {
  
  private FunctionList() {}
  
  public static List<Function> get() {
    List<Function> l = new ArrayList<>();
    Collections.addAll(l,
        new Assert(),
        new Exists(),
        new Print()
    );
    return Collections.unmodifiableList(l);
  }
}
