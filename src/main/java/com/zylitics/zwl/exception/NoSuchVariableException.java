package com.zylitics.zwl.exception;

public class NoSuchVariableException extends EvalException {
  
  private static final long serialVersionUID = -392967070074388093L;
  
  public NoSuchVariableException(String msg) {
    super(msg);
  }
  
  @SuppressWarnings("unused")
  public NoSuchVariableException(String msg, Throwable cause) {
    super(msg, cause);
  }
}