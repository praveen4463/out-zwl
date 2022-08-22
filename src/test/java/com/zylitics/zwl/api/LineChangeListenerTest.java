package com.zylitics.zwl.api;

import com.google.common.base.Charsets;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class LineChangeListenerTest {
  
  private int actualTotalLineChangeEvents = 0;
  
  @Test
  void lineChangeListenerTest() throws IOException
  {
    ZwlApi zwlApi = new ZwlApi(Paths.get("resources/" + "LineChangeListenerTest.zwl")
        , Charsets.UTF_8, ZwlLangTests.DEFAULT_TEST_LISTENERS);
    zwlApi.interpretDevOnly(null, null, null, zwlInterpreter ->
        zwlInterpreter.setLineChangeListener(line -> {
          actualTotalLineChangeEvents++;
          System.out.println("Currently executing line " + line);
        }));
    // expected is taken from the program's actual lines where there is a code (no comment or empty)
    assertEquals(7, actualTotalLineChangeEvents);
  }
}
