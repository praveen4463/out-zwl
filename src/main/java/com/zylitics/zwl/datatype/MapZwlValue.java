package com.zylitics.zwl.datatype;

import java.util.Iterator;
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
    Iterator<Map.Entry<String, ZwlValue>> i = value.entrySet().iterator();
    if (! i.hasNext())
      return "{}";
    
    StringBuilder sb = new StringBuilder();
    sb.append("{\n");
    for (;;) {
      Map.Entry<String, ZwlValue> e = i.next();
      String key = e.getKey();
      ZwlValue value = e.getValue();
      sb.append(key);
      sb.append(": ");
      sb.append(value);
      if (! i.hasNext())
        return sb.append("\n}").toString();
      sb.append(",\n");
    }
  }
}
