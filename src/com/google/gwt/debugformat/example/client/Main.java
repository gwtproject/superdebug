package com.google.gwt.debugformat.example.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;

import java.util.LinkedHashMap;
import java.util.Map;

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
    private final JavaScriptObject emptyJsObject = JavaScriptObject.createObject();
    private final JavaScriptObject jsObject = makeJsObject();
    private final Map<String, Object> aMap = makeExampleMap();

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
  }
}
