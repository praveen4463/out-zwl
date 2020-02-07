package com.zylitics.zwl.datatype;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ListZwlValue implements ZwlValue {
  
  private final List<ZwlValue> value;
  
  public ListZwlValue(List<ZwlValue> value) {
    Objects.requireNonNull(value);
    this.value = value;
  }
  
  @Override
  public Optional<List<ZwlValue>> getListValue() {
    return Optional.of(value);
  }
  
  @Override
  public String getType() {
    return "List";
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ListZwlValue that = (ListZwlValue) o;
    return value.equals(that.value);
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
