####Antlr4 grammars for ZWL

Steps to generate parsers and lexers from grammar.

1- First update the required grammar files.

2- Go to console, run mvn antlr4:antlr4

3- Parser and Lexer code should generate in target/generated-sources/antlr4

4- No need to copy the files in any other class path, compiler infers from
location using antlr4's plugin.