package com.zylitics.zwl.exception;

public class IllegalIdentifierException extends EvalException {
  
  private static final long serialVersionUID = -4271001069440112463L;
  
  public IllegalIdentifierException(String fromPos, String toPos, String msg) {
    super(fromPos, toPos, msg);
  }
  
  @SuppressWarnings("unused")
  public IllegalIdentifierException(String fromPos, String toPos, String msg, Throwable cause) {
    super(fromPos, toPos, msg, cause);
  }
}
