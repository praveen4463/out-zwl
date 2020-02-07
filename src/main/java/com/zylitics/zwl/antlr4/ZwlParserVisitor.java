// Generated from com/zylitics/zwl/antlr4/ZwlParser.g4 by ANTLR 4.8
package com.zylitics.zwl.antlr4;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ZwlParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ZwlParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ZwlParser#compilationUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompilationUnit(ZwlParser.CompilationUnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link ZwlParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(ZwlParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link ZwlParser#indexes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexes(ZwlParser.IndexesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ZwlParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(ZwlParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ZwlParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(ZwlParser.AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link ZwlParser#functionInvocation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionInvocation(ZwlParser.FunctionInvocationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ZwlParser#defaultVal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefaultVal(ZwlParser.DefaultValContext ctx);
	/**
	 * Visit a parse tree produced by {@link ZwlParser#identifierExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierExpr(ZwlParser.IdentifierExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ZwlParser#ifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(ZwlParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ZwlParser#ifBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfBlock(ZwlParser.IfBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link ZwlParser#elseIfBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseIfBlock(ZwlParser.ElseIfBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link ZwlParser#elseBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseBlock(ZwlParser.ElseBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link ZwlParser#whileStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStatement(ZwlParser.WhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ZwlParser#forStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStatement(ZwlParser.ForStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ZwlParser#forInListStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForInListStatement(ZwlParser.ForInListStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ZwlParser#forInMapStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForInMapStatement(ZwlParser.ForInMapStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ZwlParser#forToStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForToStatement(ZwlParser.ForToStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ZwlParser#increment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIncrement(ZwlParser.IncrementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ZwlParser#decrement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecrement(ZwlParser.DecrementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ZwlParser#listLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListLiteral(ZwlParser.ListLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ZwlParser#mapLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapLiteral(ZwlParser.MapLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ZwlParser#mapEntry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapEntry(ZwlParser.MapEntryContext ctx);
	/**
	 * Visit a parse tree produced by {@link ZwlParser#mapEntries}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapEntries(ZwlParser.MapEntriesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mapExpression}
	 * labeled alternative in {@link ZwlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapExpression(ZwlParser.MapExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code numberExpression}
	 * labeled alternative in {@link ZwlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberExpression(ZwlParser.NumberExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code identifierExpression}
	 * labeled alternative in {@link ZwlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierExpression(ZwlParser.IdentifierExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code notExpression}
	 * labeled alternative in {@link ZwlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpression(ZwlParser.NotExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code rawStringExpression}
	 * labeled alternative in {@link ZwlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRawStringExpression(ZwlParser.RawStringExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionInvocationExpression}
	 * labeled alternative in {@link ZwlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionInvocationExpression(ZwlParser.FunctionInvocationExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code incrementExpression}
	 * labeled alternative in {@link ZwlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIncrementExpression(ZwlParser.IncrementExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code booleanExpression}
	 * labeled alternative in {@link ZwlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanExpression(ZwlParser.BooleanExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code orExpression}
	 * labeled alternative in {@link ZwlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpression(ZwlParser.OrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unaryMinusExpression}
	 * labeled alternative in {@link ZwlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryMinusExpression(ZwlParser.UnaryMinusExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code eqExpression}
	 * labeled alternative in {@link ZwlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqExpression(ZwlParser.EqExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code andExpression}
	 * labeled alternative in {@link ZwlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpression(ZwlParser.AndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code stringExpression}
	 * labeled alternative in {@link ZwlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringExpression(ZwlParser.StringExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parenthesizedExpression}
	 * labeled alternative in {@link ZwlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesizedExpression(ZwlParser.ParenthesizedExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addExpression}
	 * labeled alternative in {@link ZwlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddExpression(ZwlParser.AddExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code compExpression}
	 * labeled alternative in {@link ZwlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompExpression(ZwlParser.CompExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code multExpression}
	 * labeled alternative in {@link ZwlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultExpression(ZwlParser.MultExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code listExpression}
	 * labeled alternative in {@link ZwlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListExpression(ZwlParser.ListExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code decrementExpression}
	 * labeled alternative in {@link ZwlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecrementExpression(ZwlParser.DecrementExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ZwlParser#expressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionList(ZwlParser.ExpressionListContext ctx);
}