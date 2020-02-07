package com.zylitics.zwl.datatype;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Encapsulates various types of data values supported by ZWL, implementing types expose the exact
 * type of the data it contains.
 */
public interface ZwlValue {
  
  default Optional<Boolean> getBooleanValue() {
    return Optional.empty();
  }
  
  default Optional<Double> getDoubleValue() {
    return Optional.empty();
  }
  
  default Optional<String> getStringValue() {
    return Optional.empty();
  }
  
  default Optional<List<ZwlValue>> getListValue() {
    return Optional.empty();
  }
  
  default Optional<Map<String, ZwlValue>> getMapValue() {
    return Optional.empty();
  }
  
  default Optional<Object> getNothingValue() {
    return Optional.empty();
  }
  
  String getType();
}
