package com.google.gwt.superdebug.example.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

import java.util.*;

/**
 * An object containing various examples to look at in the debugger.
 */
class Example {
  private final String aString = "a string";
  private final int anInt = 1234;
  private final long aLong = 12345l;
  private final JavaScriptObject emptyJsObject = JavaScriptObject.createObject();
  private final Object javaObject = new Object();
  private final JavaScriptObject jsObject = makeJsObject();
  private final Map<String, Object> aMap = makeExampleMap();
  private final List<Integer> aList = Arrays.asList(1, 2, 3);
  private final List<String> aBigList = makeStringList("hello", 120);
  private final int[] intArray = new int[] {1, 2, 3};
  private final String[] stringArray = new String[] {"one", "two", "three"};
  private final String[][] stringArray2 = new String[][] {
      {"first", "second"},
      {"zero", "one"}
  };
  private final RootPanel rootPanel;

  Example() {
    rootPanel = RootPanel.get("container");
    rootPanel.add(new Label("hello!"));
    // Force variables to exist.
    assert !Integer.toString(hashCode()).isEmpty();
  }

  @Override
  public int hashCode() {
    return aString.length() + anInt + ((int)aLong) + emptyJsObject.hashCode() + javaObject.hashCode() +
        jsObject.hashCode() + aMap.hashCode() + aList.hashCode() + aBigList.hashCode() +
        intArray.hashCode() + stringArray.hashCode() + stringArray2.hashCode();
  }

  private static native JavaScriptObject makeJsObject() /*-{
    function Thing() {}
    return {
      "aNumber": 123,
      "aString": "foo",
      "anArray": [1, 2, 3],
      "anObject": {"d": "e"},
      "aThing": new Thing()
    };
  }-*/;

  private static Map<String, Object> makeExampleMap() {
    Map<String, Object> result = new LinkedHashMap<>();
    result.put("a string", "hello");
    result.put("a long", 12345l);
    return result;
  }

  private static List<String> makeStringList(String prefix, int size) {
    List<String> out = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      out.add(prefix + String.valueOf(i));
    }
    return out;
  }
}
