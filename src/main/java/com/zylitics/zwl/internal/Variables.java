package com.zylitics.zwl.internal;

import com.zylitics.zwl.datatype.ZwlValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Variables {
  
  private final Map<String, ZwlValue> storage = new HashMap<>();
  
  public void assign(String identifier, ZwlValue value) {
    Objects.requireNonNull(identifier, "identifier can't be null");
    Objects.requireNonNull(value, "value can't be null");
    
    storage.put(identifier, value);
  }
  
  public Optional<ZwlValue> resolve(String identifier) {
    Objects.requireNonNull(identifier, "identifier can't be null");
    
    return Optional.ofNullable(storage.get(identifier));
  }
  
  public boolean exists(String identifier) {
    Objects.requireNonNull(identifier, "identifier can't be null");
    
    return storage.containsKey(identifier);
  }
  
  public void delete(String identifier) {
    Objects.requireNonNull(identifier, "identifier can't be null");
    
    if (!exists(identifier)) {
      throw new RuntimeException(
          "Trying to remove identifier " + identifier + " that doesn't exist");
    }
    storage.remove(identifier);
  }
}
