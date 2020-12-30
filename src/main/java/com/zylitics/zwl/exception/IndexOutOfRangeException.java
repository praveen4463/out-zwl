package com.zylitics.zwl.exception;

public class IndexOutOfRangeException extends EvalException {
  
  private static final long serialVersionUID = 7113987889469089669L;
  
  public IndexOutOfRangeException(String fromPos, String toPos, String msg) {
    super(fromPos, toPos, msg);
  }
}
