package com.zylitics.zwl.exception;

public class InvalidRegexPatternException extends EvalException {
  
  private static final long serialVersionUID = -2656317084188502423L;
  
  public InvalidRegexPatternException(String fromPos, String toPos, String msg) {
    super(fromPos, toPos, msg);
  }
}
