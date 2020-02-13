package com.zylitics.zwl.datatype;

import java.util.Objects;

public class VoidZwlValue implements ZwlValue {
  
  @Override
  public String getType() {
    return Types.VOID;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(Types.VOID);
  }
  
  @Override
  public String toString() {
    return Types.VOID;
  }
}
