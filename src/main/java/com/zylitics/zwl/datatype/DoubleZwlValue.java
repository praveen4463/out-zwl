package com.zylitics.zwl.datatype;

import java.text.DecimalFormat;
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
    return Types.NUMBER;
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
    // 2 hashes after decimal rounds the number of 2 after a decimal point and 0's are ignored, so
    // 1.00 will be printed as 1 and 1.01 will remain as is.
    return new DecimalFormat("#.##").format(value);
  }
}
