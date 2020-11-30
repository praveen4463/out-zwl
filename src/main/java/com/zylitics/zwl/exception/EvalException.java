package com.zylitics.zwl.exception;

/**
 Exceptions in a ZWL program caused during program evaluation, for example a
 {@link DateTimeFormatException}
 */
public class EvalException extends ZwlLangException {
  
  private static final long serialVersionUID = -2737215880481504769L;
  
  public EvalException(String fromPos, String toPos, String msg) {
    super(fromPos, toPos, msg);
  }
  
  public EvalException(String fromPos, String toPos, String msg, Throwable cause) {
    super(fromPos, toPos, msg, cause);
  }
}
