package com.zylitics.zwl.api;

import com.google.common.base.Charsets;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class AllTests {
  
  @Test
  void basicSyntaxTest() throws IOException {
    Main main = new Main("resources/BasicSyntaxTest.zwl", Charsets.UTF_8);
    main.interpretDevOnly(null);
  }
  
  @Test
  void typeConversionTest() throws Exception {
    Main main = new Main("resources/TypeConversionTest.zwl", Charsets.UTF_8);
    main.interpretDevOnly(null);
  }
  
  @Test
  void syntaxValidationTest() throws Exception {
    Main main = new Main("resources/SyntaxValidationTest.zwl", Charsets.UTF_8);
    main.interpretDevOnly(null);
  }
}
