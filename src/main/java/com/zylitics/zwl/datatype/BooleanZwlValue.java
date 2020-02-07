package com.zylitics.zwl.datatype;

import java.util.Objects;
import java.util.Optional;

public class BooleanZwlValue implements ZwlValue {
  
  private final boolean value;
  
  public BooleanZwlValue(boolean value) {
    this.value = value;
  }
  
  @Override
  public Optional<Boolean> getBooleanValue() {
    return Optional.of(value);
  }
  
  @Override
  public String getType() {
    return "Boolean";
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BooleanZwlValue that = (BooleanZwlValue) o;
    return value == that.value;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
  
  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
