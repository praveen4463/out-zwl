package com.zylitics.zwl.interpret;

public class LangTest {
  
  // create parse like this during integration tests.
  /*
  private ZwlParser getParserDevOnly(ZwlLexer lexer) {
    ZwlParser parser = getParser(lexer);
    parser.removeErrorListeners();
    parser.addErrorListener(new DiagnosticErrorListener());
    parser.getInterpreter().setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);
    return parser;
  }
   */
}
