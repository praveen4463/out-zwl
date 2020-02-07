package com.zylitics.zwl.datatype;

import java.util.Objects;

public class VoidZwlValue implements ZwlValue {
  
  @Override
  public String getType() {
    return "Void";
  }
  
  @Override
  public int hashCode() {
    return Objects.hash("Void");
  }
  
  @Override
  public String toString() {
    return "Void";
  }
}
