package com.zylitics.zwl.exception;

public class NoSuchMapKeyException extends EvalException {
  
  private static final long serialVersionUID = -2080522235735549259L;
  
  public NoSuchMapKeyException(String msg) {
    super(msg);
  }
  
  @SuppressWarnings("unused")
  public NoSuchMapKeyException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
