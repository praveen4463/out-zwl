package com.zylitics.zwl.datatype;

import java.util.Objects;
import java.util.Optional;

public class StringZwlValue implements ZwlValue {
  
  private final String value;
  
  public StringZwlValue(String value) {
    Objects.requireNonNull(value);
    
    this.value = value;
  }
  
  @Override
  public Optional<String> getStringValue() {
    return Optional.of(value);
  }
  
  @Override
  public String getType() {
    return Types.STRING;
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    StringZwlValue that = (StringZwlValue) o;
    return value.equals(that.value);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
  
  @Override
  public String toString() {
    return value;
  }
}
