package com.zylitics.zwl.exception;

public class IllegalStringFormatException extends EvalException {
  
  private static final long serialVersionUID = 1566793980796058597L;
  
  @SuppressWarnings("unused")
  public IllegalStringFormatException(String fromPos, String toPos, String msg) {
    super(fromPos, toPos, msg);
  }
  
  public IllegalStringFormatException(String fromPos, String toPos, String msg, Throwable cause) {
    super(fromPos, toPos, msg, cause);
  }
}
