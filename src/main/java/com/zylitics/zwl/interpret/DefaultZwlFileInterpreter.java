package com.zylitics.zwl.interpret;

import com.zylitics.zwl.antlr4.ZwlParser;
import com.zylitics.zwl.antlr4.ZwlParserBaseVisitor;
import com.zylitics.zwl.model.ZwlFileTest;
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
    test.setCode(ctx.block().statement().stream()
        .map(ZwlParser.StatementContext::getText).collect(Collectors.joining()));
    zwlFileTests.add(test);
    return null;
  }
  
  private String processStringLiteral(String textWithQuotes) {
    return StringEscapeUtils.unescapeJava(textWithQuotes.substring(1,
        textWithQuotes.length() - 1));
  }
}
