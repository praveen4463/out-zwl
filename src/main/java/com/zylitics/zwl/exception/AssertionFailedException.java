package com.zylitics.zwl.exception;

import com.zylitics.zwl.function.assertions.Assert;

public class AssertionFailedException extends EvalException {
  
  private static final long serialVersionUID = -1960333460358306313L;
  
  public AssertionFailedException(String msg) {
    super(msg);
  }
  
  public AssertionFailedException(String msg, Throwable cause) {super(msg, cause);}
}
