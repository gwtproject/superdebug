package com.google.gwt.debugformat.client;

import com.google.gwt.core.client.JavaScriptObject;

class HelloFormatter implements Formatter {

  @Override
  public TemplateNode header(JavaScriptObject object) {
    if (object == getTarget()) {
      TemplateBuilder b = new TemplateBuilder("span", "background-color: #fcc");
      b.text("Hello!");
      return b.build();
    } else {
      return null;
    }
  }

  @Override
  public boolean hasBody(JavaScriptObject object) {
    return false;
  }

  @Override
  public TemplateNode body(JavaScriptObject object) {
    return null;
  }

  private static native JavaScriptObject getTarget() /*-{
    return $wnd.hello;
  }-*/;
}
