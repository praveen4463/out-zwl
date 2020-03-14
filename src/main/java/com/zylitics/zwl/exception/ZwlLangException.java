package com.zylitics.zwl.exception;

/**
 * <p>Superclass of all exceptions raised in a ZWL program. Exceptions that don't subclass this,</p>
 * <p>should use constructor with 'wrapped' parameter so that the actual exception could be
 * retrieved.</p>
 * <p>Usage:</p>
 * <pre>{@code
 *  Throwable actual = null;
 *  String msg;
 *  try {
 *    // operation
 *  } catch (ZwlLangException zlEx) {
 *    if (zlEx.getWrapped() != null) {
 *      actual = zlEx.getWrapped();
 *    } else {
 *      actual = zlEx;
 *    }
 *    // actual now contains an exception directly derived from ZwlLangException, or from one of
 *    // it's subclass (such as a DateTimeFormatException). Directly derived exceptions may have
 *    // a wrapped exception which is the 'actual' exception raised. An example of a wrapped
 *    // exception may be the one raised in one of injected functions (such as a WebdriverException)
 *    msg = zlEx.getMessage();
 *    // should contain the line number and actual message that can be relayed as is.
 *  }
 * }</pre>
 */
public class ZwlLangException extends RuntimeException {
  
  private static final long serialVersionUID = -8954182436250699689L;
  
  private Throwable wrapped;
  
  public ZwlLangException(String msg) {
    super(msg);
  }
  
  public ZwlLangException(String msg, Throwable cause) {
    super(msg, cause);
  }
  
  public ZwlLangException(Throwable wrapped, String msg) {
    super(msg);
    this.wrapped = wrapped;
  }
  
  public ZwlLangException(Throwable wrapped, String msg, Throwable cause) {
    super(msg, cause);
    this.wrapped = wrapped;
  }
  
  public Throwable getWrapped() {
    return wrapped;
  }
}