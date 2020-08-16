package com.zylitics.zwl.interpret;

import com.google.common.collect.ImmutableSet;
import com.zylitics.zwl.function.assertions.Assert;
import com.zylitics.zwl.function.assertions.AssertFalse;
import com.zylitics.zwl.function.assertions.AssertTrue;
import com.zylitics.zwl.function.collection.*;
import com.zylitics.zwl.function.datetime.Elapsed;
import com.zylitics.zwl.function.datetime.FormatDate;
import com.zylitics.zwl.function.datetime.Instant;
import com.zylitics.zwl.function.datetime.Timestamp;
import com.zylitics.zwl.function.debugging.PrintF;
import com.zylitics.zwl.function.numeric.*;
import com.zylitics.zwl.function.string.*;
import com.zylitics.zwl.function.util.Exists;
import com.zylitics.zwl.function.debugging.Print;
import com.zylitics.zwl.function.util.NonEmpty;
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
        
        // Collection
        new ContainsElement(),
        new ContainsKey(),
        new ExistsFirst(),
        new Flatten(),
        new Keys(),
        new Merge(),
        new NonEmptyFirst(),
        new RemoveEmpty(),
        new RemoveElements(),
        new Size(),
        new Values(),
        
        // Datetime
        new FormatDate(),
        new Timestamp(),
        new Instant(),
        new Elapsed(),
    
        // Debugging
        new Print(),
        new PrintF(),
        
        // Numeric
        new Abs(),
        new Ceil(),
        new Floor(),
        new Max(),
        new Min(),
        
        // String
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
        new Exists(),
        new NonEmpty(),
        new Uuid()
    );
    return builder.build();
  }
}
