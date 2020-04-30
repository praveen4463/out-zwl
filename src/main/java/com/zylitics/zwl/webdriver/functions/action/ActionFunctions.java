package com.zylitics.zwl.webdriver.functions.action;

import com.zylitics.zwl.webdriver.functions.AbstractSignatureReturningFunctions;
import com.zylitics.zwl.webdriver.functions.AbstractWebdriverFunction;
import com.zylitics.zwl.datatype.ZwlValue;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class ActionFunctions {
  
  public static final String FOCUS = "focus";
  
  public static final String SHIFT_DOWN = "shiftDown";
  
  public static final String SHIFT_UP = "shiftUp";
  
  public static final String CTRL_DOWN = "ctrlDown";
  
  public static final String CTRL_UP = "ctrlUp";
  
  public static final String CMD_DOWN = "cmdDown";
  
  public static final String CMD_UP = "cmdUp";
  
  public static final String ALT_DOWN = "altDown";
  
  public static final String ALT_UP = "altUp";
  
  public static final String SEND_KEYS = "sendKeys";
  
  public static final String MOVE = "move";
  
  public static final String HOLD = "hold";
  
  public static final String RELEASE = "release";
  
  public static final String CLICK_ONCE = "clickOnce";
  
  public static final String DOUBLE_CLICK = "doubleClick";
  
  public static final String CONTEXT_CLICK = "contextClick";
  
  public static final String PAUSE = "pause";
  
  public static class Focus extends AbstractSignatureReturningFunctions implements ActionFunction {
    
    @Override
    public String getName() {
      return FOCUS;
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
    public void process(AbstractWebdriverFunction wd, Actions actions, List<ZwlValue> args) {
      actions.click(wd.getElement(wd.tryCastString(0, args.get(0))));
    }
  }
  
  public static class ShiftDown extends AbstractSignatureReturningFunctions
      implements ActionFunction {
    
    @Override
    public String getName() {
      return SHIFT_DOWN;
    }
  
    @Override
    public void process(AbstractWebdriverFunction wd, Actions actions, List<ZwlValue> args) {
      actions.keyDown(Keys.SHIFT);
    }
  }
  
  public static class ShiftUp extends AbstractSignatureReturningFunctions
      implements ActionFunction {
    
    @Override
    public String getName() {
      return SHIFT_UP;
    }
  
    @Override
    public void process(AbstractWebdriverFunction wd, Actions actions, List<ZwlValue> args) {
      actions.keyUp(Keys.SHIFT);
    }
  }
  
  public static class CtrlDown extends AbstractSignatureReturningFunctions
      implements ActionFunction {
    
    @Override
    public String getName() {
      return CTRL_DOWN;
    }
  
    @Override
    public void process(AbstractWebdriverFunction wd, Actions actions, List<ZwlValue> args) {
      actions.keyDown(Keys.CONTROL);
    }
  }
  
  public static class CtrlUp extends AbstractSignatureReturningFunctions
      implements ActionFunction {
    
    @Override
    public String getName() {
      return CTRL_UP;
    }
  
    @Override
    public void process(AbstractWebdriverFunction wd, Actions actions, List<ZwlValue> args) {
      actions.keyUp(Keys.CONTROL);
    }
  }
  
  public static class CmdDown extends AbstractSignatureReturningFunctions
      implements ActionFunction {
    
    @Override
    public String getName() {
      return CMD_DOWN;
    }
  
    @Override
    public void process(AbstractWebdriverFunction wd, Actions actions, List<ZwlValue> args) {
      actions.keyDown(Keys.COMMAND);
    }
  }
  
  public static class CmdUp extends AbstractSignatureReturningFunctions
      implements ActionFunction {
    
    @Override
    public String getName() {
      return CMD_UP;
    }
  
    @Override
    public void process(AbstractWebdriverFunction wd, Actions actions, List<ZwlValue> args) {
      actions.keyUp(Keys.COMMAND);
    }
  }
  
  public static class AltDown extends AbstractSignatureReturningFunctions
      implements ActionFunction {
    
    @Override
    public String getName() {
      return ALT_DOWN;
    }
  
    @Override
    public void process(AbstractWebdriverFunction wd, Actions actions, List<ZwlValue> args) {
      actions.keyDown(Keys.ALT);
    }
  }
  
  public static class AltUp extends AbstractSignatureReturningFunctions
      implements ActionFunction {
    
    @Override
    public String getName() {
      return ALT_UP;
    }
  
    @Override
    public void process(AbstractWebdriverFunction wd, Actions actions, List<ZwlValue> args) {
      actions.keyUp(Keys.ALT);
    }
  }
  
  public static class SendKeys extends AbstractSignatureReturningFunctions
      implements ActionFunction {
    
    @Override
    public String getName() {
      return SEND_KEYS;
    }
    
    @Override
    public int minParamsCount() {
      return 1;
    }
    
    @Override
    public int maxParamsCount() {
      return Integer.MAX_VALUE;
    }
  
    @Override
    public void process(AbstractWebdriverFunction wd, Actions actions, List<ZwlValue> args) {
      actions.sendKeys(args.stream().map(Objects::toString).toArray(String[]::new));
    }
  }
  
  public static class Move extends AbstractSignatureReturningFunctions implements ActionFunction {
    
    @Override
    public String getName() {
      return MOVE;
    }
    
    @Override
    public int minParamsCount() {
      return 1;
    }
    
    @Override
    public int maxParamsCount() {
      return 3;
    }
  
    // although Move extends AbstractFunction, access it's methods with AbstractWebdriverFunction
    // reference because when we invoke process, things like lineNumber doesn't set up.
    @Override
    public void process(AbstractWebdriverFunction wd, Actions actions, List<ZwlValue> args) {
      switch (args.size()) {
        case 1:
          actions.moveToElement(wd.getElement(wd.tryCastString(0, args.get(0))));
          break;
        case 2:
          actions.moveByOffset(wd.parseDouble(0, args.get(0)).intValue(),
              wd.parseDouble(1, args.get(1)).intValue());
          break;
        case 3:
          actions.moveToElement(wd.getElement(wd.tryCastString(0, args.get(0))),
              wd.parseDouble(1, args.get(1)).intValue(),
              wd.parseDouble(2, args.get(2)).intValue());
          break;
        default:
          throw unexpectedEndOfFunctionOverload(args.size());
      }
    }
  }
  
  public static class Hold extends AbstractSignatureReturningFunctions implements ActionFunction {
    
    @Override
    public String getName() {
      return HOLD;
    }
  
    @Override
    public void process(AbstractWebdriverFunction wd, Actions actions, List<ZwlValue> args) {
      actions.clickAndHold();
    }
  }
  
  public static class Release extends AbstractSignatureReturningFunctions
      implements ActionFunction {
    
    @Override
    public String getName() {
      return RELEASE;
    }
  
    @Override
    public void process(AbstractWebdriverFunction wd, Actions actions, List<ZwlValue> args) {
      actions.release();
    }
  }
  
  public static class ClickOnce extends AbstractSignatureReturningFunctions
      implements ActionFunction {
    
    @Override
    public String getName() {
      return CLICK_ONCE;
    }
  
    @Override
    public void process(AbstractWebdriverFunction wd, Actions actions, List<ZwlValue> args) {
      actions.click();
    }
  }
  
  public static class DoubleClick extends AbstractSignatureReturningFunctions
      implements ActionFunction {
    
    @Override
    public String getName() {
      return DOUBLE_CLICK;
    }
  
    @Override
    public void process(AbstractWebdriverFunction wd, Actions actions, List<ZwlValue> args) {
      actions.doubleClick();
    }
  }
  
  public static class ContextClick extends AbstractSignatureReturningFunctions
      implements ActionFunction {
    
    @Override
    public String getName() {
      return CONTEXT_CLICK;
    }
  
    @Override
    public void process(AbstractWebdriverFunction wd, Actions actions, List<ZwlValue> args) {
      actions.contextClick();
    }
  }
  
  public static class Pause extends AbstractSignatureReturningFunctions implements ActionFunction {
    
    @Override
    public String getName() {
      return PAUSE;
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
    public void process(AbstractWebdriverFunction wd, Actions actions, List<ZwlValue> args) {
      actions.pause(Duration.ofMillis(wd.parseDouble(0, args.get(0)).longValue()));
    }
  }
}
