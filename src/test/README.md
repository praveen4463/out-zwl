### Testing ZWL

Note: Unit tests are not written in ZWL because of extensive requirement for mocking
different parser classes which is less efficient and more error prone than writing 
integration test as they run quickly being everything is local in ZWL.

ZWL's tests are written using ZWL itself.

Tests are converted into various categories so that whenever functionality are added,
we know which part to look into for additions and removals. These are the categories
of tests written in ZWL:

1- Basic syntax test:


2- Advanced constructs test: Such as using multiple nested arrays, maps, nested loops
, nested if conditions etc.


3- Type conversion test:


4- Arithmetic/Logical operations test:


5- Built-in function test:


6- Validation tests: such as verifying illegal input throws correct exception. For example
using a variable in for loop that is declared in outer scope should throw a EvalException.


7- Parsing tests: Tests to verify parser behaviour by giving inputs like invalid
tokens, invalid constructs and verifying the exception thrown. 