package com.zylitics.zwl.interpret;

import com.google.common.collect.ImmutableSet;
import com.zylitics.zwl.function.assertions.*;
import com.zylitics.zwl.function.collection.*;
import com.zylitics.zwl.function.datetime.*;
import com.zylitics.zwl.function.debugging.PrintF;
import com.zylitics.zwl.function.numeric.*;
import com.zylitics.zwl.function.string.*;
import com.zylitics.zwl.function.debugging.Print;
import com.zylitics.zwl.function.util.NonEmpty;
import com.zylitics.zwl.function.util.RandomFromRange;
import com.zylitics.zwl.function.util.Uuid;

import java.util.Set;

public final class BuiltInFunction {
  
  private BuiltInFunction() {}
  
  public static Set<Function> get() {
    ImmutableSet.Builder<Function> builder = ImmutableSet.builder();
    builder.add(
        // Assertions
        new Assert(),
        new AssertTrue(),
        new AssertFalse(),
        new AssertEqual(),
        new AssertNotEqual(),
        
        // Collection
        new AddTo(),
        new ContainsElement(),
        new ContainsKey(),
        new ExistsFirst(),
        new Flatten(),
        new Keys(),
        new Merge(),
        new NonEmptyFirst(),
        new PutIn(),
        new RemoveAll(),
        new RemoveAt(),
        new RemoveEmpty(),
        new RemoveFrom(),
        new SetAt(),
        new Size(),
        new Values(),
        
        // Datetime
        new FormatDate(),
        new Timestamp(),
        new Instant(),
        new Elapsed(),
        new DateAdd(),
        new DateIsBefore(),
        new DateIsAfter(),
        new WithLastDayOfMonth(),
    
        // Debugging
        new Print(),
        new PrintF(),
        
        // Numeric
        new Abs(),
        new Ceil(),
        new Floor(),
        new Max(),
        new Min(),
        new ParseNum(),
        
        // String
        new CompareTo(),
        new CompareToIgnoreCase(),
        new ContainsString(),
        new EndsWith(),
        new EqualsIgnoreCase(),
        new Format(),
        new IndexOf(),
        new Join(),
        new LastIndexOf(),
        new Length(),
        new Lower(),
        new Matches(),
        new Find(),
        new Replace(),
        new ReplaceAll(),
        new ReplaceFirst(),
        new Split(),
        new StartsWith(),
        new Substring(),
        new SubstringRegex(),
        new Title(),
        new Trim(),
        new TrimEnd(),
        new TrimStart(),
        new Upper(),
        
        // Util
        new NonEmpty(),
        new Uuid(),
        new RandomFromRange()
    );
    return builder.build();
  }
}
