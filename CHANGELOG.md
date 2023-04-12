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

## v0.6.8

### Enhancements

1. Added `compareTo`, `compareToIgnoreCase`

## v0.6.9

### Bug fixes:

1. Fixed `dragAndDrop` so it works with HTML5 dnd.
2. Fixed `try-finally` problem. The exception wasn't thrown when `catch` was omitted.

## v0.6.10

### Bug fixes:

1. Fixed warning in `addTo` for list expand. Added `doNotExpandListToArgs` for a fix.
2. Fixed `putIn` problem where if a `list` was passed, it converted into args wrongly.
3. Fixed `join` so that an empty list can be passed.
4. Fixed `randomFromRange` so that it won't throw exception if the number is greater than max int.
5. Fixed `dragAndDrop` so that it doesn't try to refresh element's state if invalid. This is done
   because the updated function don't use webdriver but js.
6. Fixed `select` related functions and kept them inside error handling block that was before missing.
7. Fixed problem in stale element refresh process so that the interaction wouldn't return it's result
   as-is but keep that in an `Optional` so that the process doesn't wait if one of the interaction
   result in `false` or `null`.

## v0.6.11

### Enhancements

1. Added `matchesSnapshot`
2. User's upload directory now is organization's
3. Bumped up libraries

## v0.6.12

### Enhancements

1. Added `_global` global variable for the main purpose of argument passing.

## v0.6.13

### Enhancements

1. `_global` is now not static but given from caller so that it remains different for each build.
2. Added `call` function and various other supporting functions to be able to call functions in a
better and natural way.
3. Added ability to know Test Path of currently running test/function.
4. `matchSnapshot` now has more arguments to help. Renamed a few args.

## v0.6.14

### Enhancements

1. Added support for parsing and interpreting a zwl file system file.

## v0.6.15

### Bug fixes:

1. Fixed a bug in extracting the code from a .zwl files. Now the code is taken as-is including the
   space characters.

## v0.6.16

### Bug fixes:

1. Fixed a findElemnets timeout exception bug. Now it catches all and sends empty list.