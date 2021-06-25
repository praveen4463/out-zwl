parser grammar ZwlParser;

options {
  tokenVocab=ZwlLexer;
}

compilationUnit
  : statement* EOF
  ;

block
  : LBRACE statement* RBRACE
  ;

indexes
  : ( LBRAC expression RBRAC )+
  ;

mapKey
  : (Identifier | StringLiteral | LBRAC expression RBRAC)
  ;

statement
  : assignment
  | functionInvocation
  | ifStatement
  | forStatement
  | whileStatement
  | increment
  | decrement
  | builtInLanguageFunction
  ;

assignment
  : Identifier ASSIGN expression
  ;

// Identifier after function invocation represent a map returned by function and we're accessing
// one of it's keys.
functionInvocation
  : Identifier LPAREN expressionList? RPAREN defaultVal? indexes? ( DOT identifierExpr )?
  ;

defaultVal
  : DOUBLE_QUESTION LBRACE expression RBRACE
  ;

// only identifier, only identifier+index, identifier+index+dot > once this is given, it is required
// to repeat the exp again, so atleast identifier need to be given which means identifier+index+dot
// + identifier (or index too). This will complete one recursion. If you put a further dot, an
// identifier (or index too) will need to be given and so on. Once you stop giving a dot, recursion
// will complete. In every step, once a dot is given an Identifer (and optional index) is required.
identifierExpr
  : Identifier indexes? ( DOT identifierExpr )?
  ;

ifStatement
  : ifBlock elseIfBlock* elseBlock?
  ;

ifBlock
  : IF expression block
  ;

elseIfBlock
  : ELSE IF expression block
  ;

elseBlock
  : ELSE block
  ;

whileStatement
  : WHILE expression block
  ;

forStatement
  : forInListStatement
  | forInMapStatement
  | forToStatement
  ;

forInListStatement
  : FOR Identifier IN expression block
  ;

forInMapStatement
  : FOR Identifier COMMA Identifier IN expression block
  ;

forToStatement
  : FOR assignment TO expression block
  ;

increment
  : Identifier INC
  ;

decrement
  : Identifier DEC
  ;

builtInLanguageFunction
  : ASSERT_THROWS LPAREN expression COMMA (block | expression) (COMMA expression)? RPAREN      #assertThrowsFunc
  | ASSERT_DOES_NOT_THROW LPAREN (block | expression) (COMMA expression)? RPAREN               #assertDoesNotThrowFunc
  | EXISTS LPAREN expression RPAREN                                                            #existsFunc
  ;

listLiteral
  : LBRAC expressionList? RBRAC
  ;

mapLiteral
  : LBRACE mapEntries? RBRACE
  ;

// If map keys ain't a valid identifier, they should be kept in quotes.
mapEntry
  : mapKey COLON expression
  ;

mapEntries
  : mapEntry ( COMMA mapEntry )*
  ;

expression
  : SUB expression                                      #unaryMinusExpression
  | BANG expression                                     #notExpression
  | increment                                           #incrementExpression
  | decrement                                           #decrementExpression
  | expression op=( MUL | DIV | MOD ) expression        #multExpression
  | expression op=( ADD | SUB ) expression              #addExpression
  | expression op=( GE | LE | GT | LT ) expression      #compExpression
  | expression op=( EQUAL | NOTEQUAL ) expression       #eqExpression
  | expression AND expression                           #andExpression
  | expression OR expression                            #orExpression
  | NumberLiteral                                       #numberExpression
  | BooleanLiteral                                      #booleanExpression
  | StringLiteral                                       #stringExpression
  | RawStringLiteral                                    #rawStringExpression
  | listLiteral                                         #listExpression
  | mapLiteral                                          #mapExpression
  | functionInvocation                                  #functionInvocationExpression
  | identifierExpr                                      #identifierExpression
  | LPAREN expression RPAREN                            #parenthesizedExpression
  | builtInLanguageFunction                             #builtInLanguageFunctionExpression
  ;

expressionList
  : expression ( COMMA expression )*
  ;