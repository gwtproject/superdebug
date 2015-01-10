package com.google.gwt.debugformat.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Installs the custom formatter.
 */
public class Installer implements EntryPoint {
  @Override
  public void onModuleLoad() {
    installFormatter(wrap(new HelloFormatter()));
  }

  private static native JavaScriptObject wrap(Formatter formatter) /*-{
    var f = {};
    f.header = function (obj) {
      return formatter.@Formatter::header(Lcom/google/gwt/core/client/JavaScriptObject;)(obj);
    }
    f.hasBody = function (obj) {
      return formatter.@Formatter::hasBody(Lcom/google/gwt/core/client/JavaScriptObject;)(obj);
    }
    f.body = function (body) {
      return formatter.@Formatter::body(Lcom/google/gwt/core/client/JavaScriptObject;)(obj);
    }
    return f;
  }-*/;

  private static native void installFormatter(JavaScriptObject obj) /*-{
    $wnd.devtoolsFormatter = obj;
  }-*/;
}
