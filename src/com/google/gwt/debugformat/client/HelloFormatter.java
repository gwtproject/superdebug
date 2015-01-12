package com.google.gwt.debugformat.client;

/**
 * An example formatter that prints "Hello!" for any object stored in window.hello.
 */
class HelloFormatter implements Formatter {

  @Override
  public TemplateNode header(Any object) {
    if (object == getTarget()) {
      TemplateBuilder b = new TemplateBuilder("span", "background-color: #fcc");
      b.text("Hello!");
      return b.build();
    } else {
      return null;
    }
  }

  @Override
  public boolean hasBody(Any object) {
    return false;
  }

  @Override
  public TemplateNode body(Any object) {
    return null;
  }

  private static native Any getTarget() /*-{
    return $wnd.hello;
  }-*/;
}
