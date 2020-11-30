package com.zylitics.zwl.exception;

public class InvalidRegexPatternException extends EvalException {
  
  private static final long serialVersionUID = -2656317084188502423L;
  
  @SuppressWarnings("unused")
  public InvalidRegexPatternException(String fromPos, String toPos, String msg) {
    super(fromPos, toPos, msg);
  }
  
  public InvalidRegexPatternException(String fromPos, String toPos, String msg, Throwable cause) {
    super(fromPos, toPos, msg, cause);
  }
}
