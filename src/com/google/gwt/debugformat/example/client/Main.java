package com.google.gwt.debugformat.example.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class Main implements EntryPoint {

  public void onModuleLoad() {
    install(makeExamples());
  }

  public static native void install(Map<String, Object> examples) /*-{
    $wnd.custom = examples;
    console.log("installed window.custom");
  }-*/;

  private static Map<String, Object> makeExamples() {
    Map<String, Object> result = new LinkedHashMap<>();
    result.put("a string", "hello");
    result.put("a long", 12345l);
    return result;
  }
}
