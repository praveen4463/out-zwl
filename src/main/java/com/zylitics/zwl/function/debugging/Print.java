package com.zylitics.zwl.function.debugging;

import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Supplier;

// should write into a stream that is accessible from outside, once read the read content is deleted
// to free up space. when dry running, we will read in the end and when running on runner, we can
// read in time splits.
// During dry run, we can use a StringBuilder and read it in the end.
// For runner, Create a class that subclasses PrintStream. It will take a listener in constructor
// that is invoked on call to println and takes argument 'message'. Main class in runner will
// instantiate this PrintStream class and give a listener that creates ZwlProgramOutput model,
// where output = message, and give it to ESDB for bulk storage. This way whenever a message is
// written from either this 'Print' class or directly into the stream, it will go to ESDB. The
// webdriver functions injected into ZWL will take this stream directly, runner will use it to for
// pushing line number information.

/**
 * <p>Writes given message and a line break to the given stream. To write just a line break,
 * send empty string.</p>
 * <p>Default constructor initialise with std out, thus writes to the console. Dry runs
 * are advised to use a stream that writes to a buffer (such as a StringBuilder). Runners should
 * use a stream that sends the output to persistent storage that stores it in a buffer and
 * commits periodically.</p>
 * <p>Dry runs or runner should instantiate this class with appropriate stream and rewrite the
 * already instantiated object.</p>
 */
public class Print extends AbstractFunction {
  
  // when a list is given. we want it's string representation to be printed rather than expanded.
  @Override
  protected boolean doNotExpandListToArguments() {
    return true;
  }
  
  private final PrintStream writeTo;
  
  public Print() {
    writeTo = System.out;
  }
  
  @SuppressWarnings("unused")
  public Print(PrintStream writeTo) {
    this.writeTo = writeTo;
  }
  
  @Override
  public String getName() {
    return "print";
  }
  
  @Override
  public int minParamsCount() {
    return 1;
  }
  
  @Override
  public int maxParamsCount() {
    return 1;
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
    int argsCount = args.size();
    
    if (argsCount == 1) {
      print(args.get(0).toString());
      return _void;
    }
  
    throw unexpectedEndOfFunctionOverload(argsCount);
  }
  
  private void print(String message) {
    writeTo.println(message);
  }
}
