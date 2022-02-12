lexer grammar ZwlLexer;

// Keywords
IF : 'if';
ELSE : 'else';
FOR : 'for';
WHILE : 'while';
IN : 'in';
TO : 'to';
ASSERT_THROWS : 'assertThrows';
ASSERT_DOES_NOT_THROW : 'assertDoesNotThrow';
EXISTS: 'exists';
TRY: 'try';
CATCH: 'catch';
FINALLY: 'finally';

// Separators
LBRACE : '{';
RBRACE : '}';
LPAREN : '(';
RPAREN : ')';
LBRAC : '[';
RBRAC : ']';
COMMA : ',';
DOT : '.';
QUOTE : '"';
SINGLE_QUOTE : '\'';
BACKTICK : '`';

// Operators
ASSIGN : '=';
GT : '>';
LT : '<';
BANG : '!';
DOUBLE_QUESTION : '??';
EQUAL : '==';
LE : '<=';
GE : '>=';
NOTEQUAL : '!=';
AND : '&&';
OR : '||';
INC : '++';
DEC : '--';
ADD : '+';
SUB : '-';
MUL : '*';
DIV : '/';
MOD : '%';
COLON : ':';

NumberLiteral
  : Integer ( DOT DIGIT+ )?
  | DOT DIGIT+
  ;

fragment
Integer
  : [1-9] DIGIT*
  | '0'
  ;

fragment
DIGIT
  : [0-9]
  ;

BooleanLiteral
  : 'true'
  | 'false'
  ;

// backquote inside raw string isn't allowed, everything else is fine. No escape sequence are
// interpreted. To still put a backquote use expression like `hi` + "`" + `praveen`
RawStringLiteral
  : BACKTICK .*? BACKTICK
  ;

// To add an expression with string, use concat(string[]) function or '+' concatenation
StringLiteral
  : QUOTE StringCharacters_Quote?? QUOTE
  | SINGLE_QUOTE StringCharacters_Single_Quote?? SINGLE_QUOTE
  ;

fragment
StringCharacters_Quote
  : StringCharacter_Quote+
  ;

fragment
StringCharacters_Single_Quote
  : StringCharacter_Single_Quote+
  ;

fragment
HexDigit
  : [0-9a-fA-F]
  ;

// match any char other than quote, backslash (means no escape sequence) and new line
// match t, n, r, quote and backslash escape sequences
// match unicode characters from basic multilingual plane.
// The match work like, every match rule matches a single character and once that char is matched,
// it matches other characters using other rules. For example we could write \u32F1praveen, u\32F1
// is matched as unicode char in BMP and praveen as a different char string.
fragment
StringCharacter_Quote
  : ~["\\\r\n]
  | '\\' [tnr"\\]
  | '\\' 'u' HexDigit HexDigit HexDigit HexDigit
  ;

fragment
StringCharacter_Single_Quote
  : ~['\\\r\n]
  | '\\' [tnr'\\]
  | '\\' 'u' HexDigit HexDigit HexDigit HexDigit
  ;

// Keep at bottom, section 5.5, Matching Identifiers
Identifier
  : [a-zA-Z_][0-9a-zA-Z_]*
  ;

// Whitespace and comments
WS
  : [ \t\r\n\u000C]+ -> skip
  ;

COMMENT
  : ( '#' ~[\r\n]* | '/*' .*? '*/' ) -> skip
  ;