package com.google.gwt.debugformat.example.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;

import java.util.*;

public class Main implements EntryPoint {

  public void onModuleLoad() {
    install(new Example());
  }

  public static native void install(Example example) /*-{
    $wnd.custom = example;
    console.log("installed window.custom");
  }-*/;

  private static class Example {
    private final String aString = "a string";
    private final int anInt = 1234;
    private final long aLong = 12345l;
    private final JavaScriptObject emptyJsObject = JavaScriptObject.createObject();
    private final JavaScriptObject jsObject = makeJsObject();
    private final Map<String, Object> aMap = makeExampleMap();
    private final List<Integer> aList = Arrays.asList(1, 2, 3);
    private final List<String> aBigList = makeStringList("hello", 120);

    private static native JavaScriptObject makeJsObject() /*-{
      function Thing() {}
      return {"a": 123, "b": 456, "c": {"d": "e"}, "thing": new Thing()};
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
}
