package com.zylitics.zwl.api;

import com.zylitics.zwl.util.StringUtil;
import org.antlr.v4.runtime.LexerNoViableAltException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Parsing errors occur during the parse tree creation, before interpretation. Thus we can't use
 * ZWL to testing parser. We must use java.
 */
public class ParsingTest {
  
  @Test
  void basicTokenRecognitionErrorTest() {
    String code = String.join(StringUtil.getPlatformLineSeparator(),
        "a = @10");
  
    try {
      Main main = new Main(code);
      main.interpretDevOnly(null);
    } catch (RuntimeException r) {
      assertTrue(ExceptionUtils.indexOfThrowable(r, LexerNoViableAltException.class) != -1);
    }
    fail();
  }
}
