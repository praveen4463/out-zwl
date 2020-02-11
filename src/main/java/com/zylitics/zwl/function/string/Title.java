package com.zylitics.zwl.function.string;

import com.zylitics.zwl.datatype.StringZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;
import org.apache.commons.lang3.text.WordUtils;

import java.util.List;
import java.util.function.Supplier;

/**
Same as https://www.terraform.io/docs/configuration/functions/title.html
 */
public class Title extends AbstractFunction {
  
  @Override
  public String getName() {
    return "title";
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
      return new StringZwlValue(title(tryCastString(0, args.get(0))));
    }
  
    throw unexpectedEndOfFunctionOverload(argsCount);
  }
  
  private String title(String s) {
    return WordUtils.capitalize(s);
  }
}
