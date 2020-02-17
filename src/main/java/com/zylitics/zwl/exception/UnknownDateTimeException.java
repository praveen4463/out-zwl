package com.zylitics.zwl.exception;

public class UnknownDateTimeException extends EvalException {
  
  private static final long serialVersionUID = -4544771620208961610L;
  
  @SuppressWarnings("unused")
  public UnknownDateTimeException(String msg) {
    super(msg);
  }
  
  public UnknownDateTimeException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
