package com.zylitics.zwl.function.debugging;

import com.zylitics.zwl.exception.IllegalStringFormatException;
import com.zylitics.zwl.function.string.Format;

import java.io.PrintStream;
import java.util.IllegalFormatException;

/**
 * PrintF prints the given string using the given format and a line break. It accepts the same
 * parameters as in {@link Format}. The default stream to print is stdout but implementations are
 * free to set the desired stream.
 * @see Print
 */
public class PrintF extends Format {
  
  private final PrintStream writeTo;
  
  public PrintF() {
    writeTo = System.out;
  }
  
  @SuppressWarnings("unused")
  public PrintF(PrintStream writeTo) {
    this.writeTo = writeTo;
  }
  
  @Override
  public String getName() {
    return "printF";
  }
  
  @Override
  protected String format(String s, Object... args) {
    try {
      //noinspection RedundantStringFormatCall
      writeTo.println(String.format(s, args));
    } catch (IllegalFormatException i) {
      throw new IllegalStringFormatException(fromPos.get(), toPos.get(),
          withLineNCol(i.getMessage()), i);
    }
    return "";
  }
}
