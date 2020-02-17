package com.zylitics.zwl.exception;

public class InsufficientArgumentsException extends EvalException {
  
  private static final long serialVersionUID = -957380948215727616L;
  
  public InsufficientArgumentsException(String msg) {
    super(msg);
  }
  
  @SuppressWarnings("unused")
  public InsufficientArgumentsException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
