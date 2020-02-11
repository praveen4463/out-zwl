package com.zylitics.zwl.exception;

import java.time.format.DateTimeFormatter;

public class UnknownDateTimeException extends EvalException {
  
  private static final long serialVersionUID = -4544771620208961610L;
  
  public UnknownDateTimeException(String msg) {
    super(msg);
  }
  
  public UnknownDateTimeException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
