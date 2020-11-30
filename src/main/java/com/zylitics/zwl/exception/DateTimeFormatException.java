package com.zylitics.zwl.exception;

public class DateTimeFormatException extends EvalException {
  
  private static final long serialVersionUID = -4544771620208961610L;
  
  @SuppressWarnings("unused")
  public DateTimeFormatException(String fromPos, String toPos, String msg) {
    super(fromPos, toPos, msg);
  }
  
  public DateTimeFormatException(String fromPos, String toPos, String msg, Throwable cause) {
    super(fromPos, toPos, msg, cause);
  }
}
