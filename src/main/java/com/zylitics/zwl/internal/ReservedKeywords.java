package com.zylitics.zwl.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReservedKeywords {
  
  public static final List<String> RESERVED_KEYWORDS = Collections.unmodifiableList(get());
  
  private static List<String> get() {
    List<String> reserved = new ArrayList<>();
    reserved.add("Nothing");
    reserved.add("Void");
    reserved.forEach(s -> s = s.toLowerCase()); // make sure all elements are lower case
    return reserved;
  }
}
