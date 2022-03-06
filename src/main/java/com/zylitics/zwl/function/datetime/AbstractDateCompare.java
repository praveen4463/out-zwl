package com.zylitics.zwl.function.datetime;

import com.google.common.base.Strings;
import com.zylitics.zwl.datatype.BooleanZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.exception.EvalException;
import com.zylitics.zwl.exception.UnknownDateTimeException;
import com.zylitics.zwl.function.AbstractFunction;
import com.zylitics.zwl.util.DateTimeUtil;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractDateCompare extends AbstractFunction {
  
  @Override
  public int minParamsCount() {
    return 2;
  }
  
  @Override
  public int maxParamsCount() {
    return 2;
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
    int argsCount = args.size();
    
    if (args.size() != 2) {
      throw unexpectedEndOfFunctionOverload(argsCount);
    }
    
    String date1 = tryCastString(0, args.get(0));
    String date2 = tryCastString(1, args.get(1));
  
    if (Strings.isNullOrEmpty(date1) || Strings.isNullOrEmpty(date2)) {
      throw new EvalException(fromPos.get(), toPos.get(),
          withLineNCol("Dates must be non empty"));
    }
  
    Function<String, UnknownDateTimeException> dateExFunc = (exMsg) ->
        new UnknownDateTimeException(fromPos.get(), toPos.get(), withLineNCol(exMsg));
  
    ZonedDateTime dateTime1 = DateTimeUtil.tryParseDate(date1, dateExFunc);
    ZonedDateTime dateTime2 = DateTimeUtil.tryParseDate(date2, dateExFunc);
    
    return new BooleanZwlValue(compare(dateTime1, dateTime2));
  }
  
  protected abstract boolean compare(ZonedDateTime dateTime1, ZonedDateTime dateTime2);
}
