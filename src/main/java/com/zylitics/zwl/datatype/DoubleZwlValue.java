package com.zylitics.zwl.datatype;

import java.util.Objects;
import java.util.Optional;

public class DoubleZwlValue implements ZwlValue {
  
  private final double value;
  
  public DoubleZwlValue(double value) {
    this.value = value;
  }
  
  @Override
  public Optional<Double> getDoubleValue() {
    return Optional.of(value);
  }
  
  @Override
  public String getType() {
    return "Number";
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DoubleZwlValue that = (DoubleZwlValue) o;
    return value == that.value;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
  
  @Override
  public String toString() {
    String v = String.valueOf(value);
    if (v.endsWith(stringToTrim)) {
      s = s.substring(0, s.lastIndexOf(stringToTrim));
    }
    return String.valueOf(value);
  }
}
