package com.zylitics.zwl.exception;

public class DateTimeFormatException extends EvalException {
  
  private static final long serialVersionUID = -4544771620208961610L;
  
  @SuppressWarnings("unused")
  public DateTimeFormatException(String msg) {
    super(msg);
  }
  
  public DateTimeFormatException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
