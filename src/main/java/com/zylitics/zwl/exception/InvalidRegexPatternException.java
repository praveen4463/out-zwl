package com.zylitics.zwl.exception;

public class InvalidRegexPatternException extends EvalException {
  
  private static final long serialVersionUID = -2656317084188502423L;
  
  @SuppressWarnings("unused")
  public InvalidRegexPatternException(String msg) {
    super(msg);
  }
  
  public InvalidRegexPatternException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
