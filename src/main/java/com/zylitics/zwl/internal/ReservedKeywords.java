package com.zylitics.zwl.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ReservedKeywords {
  
  public static final List<String> RESERVED_KEYWORDS = Collections.unmodifiableList(get());
  
  private static List<String> get() {
    List<String> reserved = new ArrayList<>();
    reserved.add("Nothing");
    reserved.add("Void");
    return reserved.stream().map(String::toLowerCase).collect(Collectors.toList());
  }
}
