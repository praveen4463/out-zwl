package com.zylitics.zwl.exception;

public class NoSuchVariableException extends EvalException {
  
  private static final long serialVersionUID = -392967070074388093L;
  
  public NoSuchVariableException(String fromPos, String toPos, String msg) {
    super(fromPos, toPos, msg);
  }
  
  @SuppressWarnings("unused")
  public NoSuchVariableException(String fromPos, String toPos, String msg, Throwable cause) {
    super(fromPos, toPos, msg, cause);
  }
}