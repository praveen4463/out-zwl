
Zylitics Webdriver Language (ZWL) is a language for building webdriver tests that is extremely
easy and has no learning curve.

Notes:
1. After changes to grammar files, use `mvn antlr4:antlr4` to generate Parser
   and Lexer code.
2. Parser and Lexer are generated under /target and inferred into classpath from
   there, no need to copy them anywhere else.
3. To publish jar, use `mvn install` or `mvn install -DskipTest=true` (if tests
   run isn't required)

- Author: Praveen Tiwari
