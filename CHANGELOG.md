## v0.1.0

All basic support for first version done, including an api to access from outside.

## v0.1.1

Fixed warnings in several locations.

## v0.1.2

1. Added functions to support calculation of elapsed time in running some code block.
2. Added support for line change listener in interpreter. Now on every line change in interpreter,
   caller's provided listener is executed that helps doing things like, outputting current line to
   user, checking thread interruption etc.

## v0.2.0

1. ZwlLangException is now the superclass of all exceptions in Zwl (not EvalException).
2. Print function now supports PrintStream as constructor parameter, enabling different type of
   consumers to provide their own PrintStream implementation on runtime rather than having to
   override the entire object with new implementation of Print function itself.
3. Fixed 'for loop' detection code that was expecting a numeric, it should be converting from string
4. added support for expanding list to arguments at runtime when user supplies a function with
   arguments as list.
5. Fixes to interpreter, Using sets rather than list wherever possible, also using linkedlist for
   maps to get ordered retrieval.
6. ZwlLangException uses a cause field to indicate exception chain. Fixes to ParseUtil so that it
   won't require an exception as argument but supplier to enable lazy instantiation.
7. Fixed raw string no carriage return on windows, a bug that causes invalid variable delete error
   when collection is empty.
8. All webdriver functions are ported from btbr into zwl, support for dry running is fully enabled,
   now zwl contains everything language feature in itself.
9. Fully upgraded the contract of ZwlApi to make it more clearer, now clients of ZwlApi will see
   what is required thru interfaces for various calls like parse, dry run, interpret.
10. Redundant dependencies fixed.

## v0.4.0

1. Build variables are now supported and can be passed to interpreter.
2. By's now have full names rather than acronyms, new by's text, role, testId, title added.
3. Added new actions functions CmdCtrlDown, CmdCtrlUp to let cmd related functions work in all platforms.
4. Functions that uses just selectors now accept fromElement, using, by so that users dont just have to use a css selector
5. Fixed tests to use functions like elementExists/untilTotalElementsGT as they now accept bys and parent element
6. Functions that must use only css selector are deprecated
7. Collections manipulation functions added are addTo, putIn, removeAt, removeFrom, removeAll, setAt
8. removeElements renamed to removeFrom and now accepts map together with list
9. wrote tests for all new functions
10. single quoted strings are now supported
11. added browsers and platforms constants
12. exists function now added at interpreter level to support non existent variables
13. length now support length of string together with size of collections
14. new functions clearActive, dblClick
15. while loop will now be broken after 100000 iterations by default
16. untilTotalElements.. functions default value fixed to list of elements

## v0.4.1

1. whenever some error occurs, a from and to positions are kept with exception detail that can later be retrieved from exception itself
2. fixed bug in which various exceptions were using a zwlLangException cause overload
3. updates in line:ch handling in exceptions
4. Unit in elapsed now have full unit names, wrote function definitions

## v0.4.2

1. fixed byText js so it supports IE, using a minified version of it now

## v0.4.3

Minor enhancements and bug fixes

### Enhancements:

1. Added support to `aria-label` selector type.
2. Added support for variables as `map` key using bracket notation.

### Bug fixes:

1. Added missing `timeUnit` read only variable used in functions like `elapsed`.
2. Fixed `map.toString` notation same as our syntax.
3. Fixed printing of a `list`.
4. Fixed `nonEmpty` so that it can tell a `list` is non-empty.

## v0.4.4

bug fixes

### Bug fixes:

1. Fixed a bug in `untilClicked` so that it catches all type of invalid element state error.

## v0.4.5

Enhancements

1. `typeActive` will now wait until there is a focused text field found
2. `type` and other similar functions will wait until the element become typeable

## v0.6.0

Major Enhancements and bug fixes

Enhancements
1. Added new element finding methods. These are labelText, altText, placeholderText.
2. Fixed element finding methods to be able to use regex. These are title, arialLabel.
3. All methods that access and element will now handle stale ex on their own and if it occurs, we'll
   try to find element internally.
4. All methods that interact with elements will now wait until interaction has happened so that if
   some element is no ready, we will wait until it is.
5. Added randomRange, dateAdd, assertEqual, assertNotEqual functions.
6. removed some functions that were useless and making the code brittle. These are activeElement,
   clearActive, findElementsWithSelectors, findElementWithSelectors, typeUsingMap, untilAllRemoved.
   
Bug fixes
1. We were not handling StaleEx at all and there was no way to handle them too.

## v0.6.1

Minor Enhancements

1. Send a custom and understandable message back to user when their code has frozen
   the browser.

## v0.6.2

Minor Enhancements

1. Added callTest function

## v0.6.3

### Bug fixes:

1. Fixed callTest function to handle file related error properly.

## v0.6.4

### Minor Enhancements

1. Added `deviceWidth` and `breakpoints` readonlyvar to support mobile emulation.

## v0.6.5

### Decent Enhancements

1. Added `try-catch-finally`.

## v0.6.6

Patch

1. Catching only `ZwlLangException` in `try-catch-finally`.

## v0.6.7

### Bug fixes:

1. Allow ability to omit catch from `try-finally`
2. The empty error (when the ex is webdriver's) problem in catch is resolved.

### Enhancements

1. Added `dateIsAfter`, `dateIsBefore`, `withLastDayOfMonth`, `parseNum`.