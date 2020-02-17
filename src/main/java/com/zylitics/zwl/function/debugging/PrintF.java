package com.zylitics.zwl.function.debugging;

import com.zylitics.zwl.exception.IllegalStringFormatException;
import com.zylitics.zwl.function.string.Format;

import java.util.IllegalFormatException;

/**
 * PrintF prints the given string using the given format and a line break. It accepts the same
 * parameters as in {@link Format}.
 */
public class PrintF extends Format {
  
  @Override
  public String getName() {
    return "printF";
  }
  
  @Override
  protected String format(String s, Object... args) {
    try {
      System.out.println(String.format(s, args));
    } catch (IllegalFormatException i) {
      throw new IllegalStringFormatException(withLineNCol(i.getMessage()), i);
    }
    return "";
  }
}
