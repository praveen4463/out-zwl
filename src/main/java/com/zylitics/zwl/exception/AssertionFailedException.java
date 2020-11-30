package com.zylitics.zwl.exception;

public class AssertionFailedException extends EvalException {
  
  private static final long serialVersionUID = -1960333460358306313L;
  
  public AssertionFailedException(String fromPos, String toPos, String msg) {
    super(fromPos, toPos, msg);
  }
  
  @SuppressWarnings("unused")
  public AssertionFailedException(String fromPos, String toPos, String msg, Throwable cause) {
    super(fromPos, toPos, msg, cause);
  }
}
