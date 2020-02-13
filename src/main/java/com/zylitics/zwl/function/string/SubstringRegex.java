package com.zylitics.zwl.function.string;

import com.zylitics.zwl.datatype.ListZwlValue;
import com.zylitics.zwl.datatype.MapZwlValue;
import com.zylitics.zwl.datatype.StringZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.exception.InvalidRegexPatternException;
import com.zylitics.zwl.function.AbstractFunction;
import com.zylitics.zwl.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Matcher;

/*
Notes:
The behaviour is like https://www.terraform.io/docs/configuration/functions/regexall.html. Methods
are inspired from https://www.terraform.io/docs/configuration/functions/regex.html
Regex info, options, flags etc should be taken from:
https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#groupname
// Also do format and title methods, all replace methods
*/
// TODO: while writing usage description, try mentioning all the features java supports including
//  unicode char classes support, unicode case folding etc.
/**
 * <p>SubstringRegex selects substring from a given string using a regex pattern. The type of value
 * returned by this function depends on the given regex.</p>
 * <p>1. If the regex contains no capture groups, the result is a list containing subsequence
 * matches by the pattern as whole.</p>
 * <p>2. If the regex contains unnamed capture group, substring matched by the groups are added
 * into a unique list for every subsequence matched. Those lists are then added into another list
 * which is returned, i.e a list of lists is returned.</p>
 * <p>3. If the regex contains named capture group, substring matched by the groups are added into
 * a unique map for every subsequence matched with map's key being the group name and map's value
 * being matched substring. Those maps are then added into a list which is returned, i.e a list of
 * maps is returned.</p>
 * <p>4. If the pattern doesn't match the input, an empty list is returned.</p>
 * <p></p>
 * <p>Regular Expression pattern:</p>
 * <p>The regex pattern must be a {@link String} with no forward slashes around the pattern. Flags
 * can be given using syntax (?idmsuxU)X, named groups are written with syntax (?<name>X).</p>
 * <p>It's not valid to mix both named and unnamed capture groups in the same pattern.</p>
 */
public class SubstringRegex extends AbstractFunction {
  
  @Override
  public String getName() {
    return "substringRegex";
  }
  
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
    
    if (argsCount == 2) {
      return substringRegex(tryCastString(0, args.get(0)), tryCastString(1, args.get(1)));
    }
  
    throw unexpectedEndOfFunctionOverload(argsCount);
  }
  
  private ZwlValue substringRegex(String s, String regex) {
    Matcher matcher = getPattern(regex).matcher(s);
    // Note: don't check whether there is a match (invoking find()) because it will advance the
    // matcher in input, we have find() in while loop in every situation, and if find returned false
    //, we always send an empty list.
    
    int groupCount = matcher.groupCount();
    
    // if there are no groups, we return a flat list with all the subsequence matches as elements
    // that matched the pattern as whole.
    if (groupCount == 0) {
      List<ZwlValue> flat = new ArrayList<>();
      while (matcher.find()) {
        flat.add(new StringZwlValue(matcher.group(0)));
      }
      return new ListZwlValue(flat);
    }
    
    /*
    If there are named capture groups, we create a map for each subsequence matched. Groups those
    were matched in every subsequence match will be put into the map with group name as map's key
    and matched value and map's value, groups those didn't match, (for example a pattern like
    "(?<alphabetic>[a-z]+)|(?<numeric>[0-9]+)" matches either alphabetic input or numeric input
    but not both in one match iteration, so only one of the group name will have a match value
    associated in each subsequence match) will not be put in the map. Users should check
    existence of a key before accessing map's value. Each map is put into a list that is finally
    returned.
     */
    List<String> captureGroupNames = StringUtil.getAllCaptureGroupNamesInRegex(regex);
    if (captureGroupNames.size() > 0) {
      if (groupCount != captureGroupNames.size()) {
        throw new InvalidRegexPatternException(String.format("Pattern contains both named and " +
            "unnamed capture groups which is not valid. Total named groups: %d, Total " +
            "unnamed groups: %d. %s", captureGroupNames.size(), groupCount, lineNColumn.get()));
      }
      List<ZwlValue> listOfMaps = new ArrayList<>();
      while (matcher.find()) {
        Map<String, ZwlValue> map = new HashMap<>();
        for (String groupName : captureGroupNames) {
          String match = matcher.group(groupName);
          if (match != null) {
            map.put(groupName, new StringZwlValue(match));
          }
        }
        listOfMaps.add(new MapZwlValue(map));
      }
      return new ListZwlValue(listOfMaps);
    }
  
    /*
    If there are no named groups, we create a list for each subsequence matched. Only groups those
    were matched in every subsequence match will be put into the list in the same order as they
    appear in the pattern. Groups those didn't match will not be in the list. Each list is put into
    a list that is finally returned. Users shouldn't identify group values in list based on index
    because unmatched matched will not be in the list, if they need to identify group values, named
    groups should be used instead so that they could access groups by names.
     */
    List<ZwlValue> listOfLists = new ArrayList<>();
    while (matcher.find()) {
      List<ZwlValue> list = new ArrayList<>();
      for (int i = 1; i <= groupCount; i++) {
        String match = matcher.group(i);
        if (match != null) {
          list.add(new StringZwlValue(match));
        }
      }
      listOfLists.add(new ListZwlValue(list));
    }
    return new ListZwlValue(listOfLists);
  }
}
