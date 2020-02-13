package com.zylitics.zwl.datatype;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class MapZwlValue implements ZwlValue {
  
  private final Map<String, ZwlValue> value;
  
  public MapZwlValue(Map<String, ZwlValue> value) {
    Objects.requireNonNull(value);
    this.value = value;
  }
  
  @Override
  public Optional<Map<String, ZwlValue>> getMapValue() {
    return Optional.of(value);
  }
  
  @Override
  public String getType() {
    return Types.MAP;
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MapZwlValue that = (MapZwlValue) o;
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
