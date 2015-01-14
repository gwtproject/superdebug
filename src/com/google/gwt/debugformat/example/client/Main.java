package com.google.gwt.debugformat.example.client;

import com.google.gwt.core.client.EntryPoint;

public class Main implements EntryPoint {

  public void onModuleLoad() {
    Example ex = new Example();
    install(ex);
  }

  public static native void install(Example example) /*-{
    $wnd.custom = example;
    console.log("installed window.custom");
  }-*/;

}
