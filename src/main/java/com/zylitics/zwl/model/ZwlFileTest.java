package com.zylitics.zwl.model;

public class ZwlFileTest {
  
  private String testName;
  
  private String code;
  
  public String getTestName() {
    return testName;
  }
  
  public ZwlFileTest setTestName(String testName) {
    this.testName = testName;
    return this;
  }
  
  public String getCode() {
    return code;
  }
  
  public ZwlFileTest setCode(String code) {
    this.code = code;
    return this;
  }
  
  @Override
  public String toString() {
    return "ZwlFileTest{" +
        "testName='" + testName + '\'' +
        ", code='" + code + '\'' +
        '}';
  }
}
