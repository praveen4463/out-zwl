package com.zylitics.zwl.interpret;

import com.zylitics.zwl.antlr4.ZwlParser;
import com.zylitics.zwl.antlr4.ZwlParserBaseVisitor;
import com.zylitics.zwl.model.ZwlFileTest;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;
import org.apache.commons.text.StringEscapeUtils;

import java.util.List;
import java.util.stream.Collectors;

public class DefaultZwlFileInterpreter extends ZwlParserBaseVisitor<Void> {
  
  private final List<ZwlFileTest> zwlFileTests;
  
  public DefaultZwlFileInterpreter(List<ZwlFileTest> zwlFileTests) {
    this.zwlFileTests = zwlFileTests;
  }
  
  @Override
  public Void visitTestDefinition(ZwlParser.TestDefinitionContext ctx) {
    ZwlFileTest test = new ZwlFileTest();
    test.setTestName(processStringLiteral(ctx.StringLiteral().getText()));
    
    // https://stackoverflow.com/a/50444757/1624454
    // TODO: There is currently a bug with this that raw string fails if they end with no leading
    //  space (see AdvanceSyntaxTest.zwl - # list and map print doesn't throw error block)
    //  cause the files put an indentation in the beginning of every statement. Later we can take
    //  every statement, get the start and end, and skip the leading space to resolve this.
    Token start = ctx.block().getStart();
    Token stop = ctx.block().getStop();
    CharStream cs = start.getTokenSource().getInputStream();
    int startIndex = start.getStartIndex() + 1; // Skip the LBRACE
    int stopIndex = stop.getStopIndex() - 1; // Skip the RBRACE
    
    test.setCode(cs.getText(new Interval(startIndex, stopIndex)));
    zwlFileTests.add(test);
    return null;
  }
  
  private String processStringLiteral(String textWithQuotes) {
    return StringEscapeUtils.unescapeJava(textWithQuotes.substring(1,
        textWithQuotes.length() - 1));
  }
}
