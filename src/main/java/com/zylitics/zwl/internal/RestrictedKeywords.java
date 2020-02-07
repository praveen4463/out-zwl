package com.zylitics.zwl.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RestrictedKeywords {
  
  public static final List<String> RESERVED = Collections.unmodifiableList(getReserved());
  
  private static List<String> getReserved() {
    List<String> reserved = new ArrayList<>();
    reserved.add("Nothing");
    reserved.add("Void");
    reserved.forEach(s -> s = s.toLowerCase()); // make sure all elements are lower case
    return reserved;
  }
}
