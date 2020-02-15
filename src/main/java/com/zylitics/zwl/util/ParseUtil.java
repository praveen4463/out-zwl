package com.zylitics.zwl.util;

import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.exception.InvalidTypeException;

public class ParseUtil {
  
  private ParseUtil() {}
  
  public static Double parseDouble(ZwlValue z, InvalidTypeException throwIfFailed) {
    if (z.getDoubleValue().isPresent()) {
      return z.getDoubleValue().get();
    }
  
    if (z.getStringValue().isPresent()) {
      try {
        return Double.parseDouble(z.getStringValue().get());
      } catch (NumberFormatException ignore) {
        // ignore
      }
    }
    
    throw throwIfFailed;
  }
  
  public static Boolean parseBoolean(ZwlValue z, InvalidTypeException throwIfFailed) {
    if (z.getBooleanValue().isPresent()) {
      return z.getBooleanValue().get();
    }
  
    if (z.getStringValue().isPresent()) {
      String s = z.getStringValue().get();
      // Boolean.parseBoolean doesn't throw ex if the string is other than true/false, thus we check
      // when we get a false result, whether the string represent a "false". If not we don't return
      // any result cause the string isn't a convertible boolean. We need 'type' exception thrown.
      boolean parsed = Boolean.parseBoolean(s);
      if (parsed || s.equalsIgnoreCase("false")) {
        return parsed;
      }
    }
    
    throw throwIfFailed;
  }
  
  @SuppressWarnings("OptionalGetWithoutIsPresent")
  public static boolean isNonEmpty(ZwlValue z) {
    switch (z.getType()) {
      case Types.BOOLEAN:
      case Types.NUMBER:
        return true;
      case Types.STRING:
        return z.getStringValue().get().trim().length() > 0;
      case Types.LIST:
        return z.getListValue().get().size() > 0;
      case Types.MAP:
        return z.getMapValue().get().size() > 0;
      default:
        return false;
    }
  }
}
