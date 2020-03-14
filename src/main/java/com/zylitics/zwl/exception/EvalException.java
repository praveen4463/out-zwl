package com.zylitics.zwl.exception;

/**
 Exceptions in a ZWL program caused during program evaluation, for example a
 {@link DateTimeFormatException}
 */
public class EvalException extends ZwlLangException {
  
  private static final long serialVersionUID = -2737215880481504769L;
  
  public EvalException(String msg) {
    super(msg);
  }
  
  public EvalException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
