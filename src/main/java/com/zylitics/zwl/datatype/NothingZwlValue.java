package com.zylitics.zwl.datatype;

import java.util.Objects;
import java.util.Optional;

/**
 * ZWL doesn't have a 'null' type and users can't assign null to a variable. For users to be still
 * able to check whether something is present and for functions to return a value that didn't exist,
 * we needed a type that points to non-existent thing. 'Nothing' is that type. If users points to
 * some value that doesn't really exist, we convert that to type 'Nothing'. This allows checks like
 * function 'exists' to know that the given value doesn't exist.
 * For example, user has a map with key 'name'. They wrote a statement map.age, which will fail
 * since the key is non existent, so they can make a check like if exists(map.age) {}. Our
 * interpreter will first evaluate map.age, upon finding that key is non-existent, it will convert
 * it to Nothing type and submit to function. The function would then check if the given type is
 * Nothing and if true, if tells the value doesn't exist hence the block wouldn't run.
 * We need to identify places that can yield a null value and convert that value to this type,
 * similarly the functions that return or produce a null, should return a Nothing object instead.
 * Places that expects concrete types like Boolean or Numbers should report users (if the
 * expectation failed) with the actual type so that if the value was Nothing they know they had to
 * use an exists check.
 */
public class NothingZwlValue implements ZwlValue {
  
  private final Object value;
  
  public NothingZwlValue() {
    value = new Object();
  }
  
  @Override
  public Optional<Object> getNothingValue() {
    return Optional.of(value);
  }
  
  @Override
  public String getType() {
    return Types.NOTHING;
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NothingZwlValue that = (NothingZwlValue) o;
    return value == that.value;
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
