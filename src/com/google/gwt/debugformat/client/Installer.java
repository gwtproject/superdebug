package com.google.gwt.debugformat.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Installs the custom formatter.
 */
public class Installer implements EntryPoint {
  @Override
  public void onModuleLoad() {
    //Formatter f = new HelloFormatter();
    Formatter f = new JavaFormatter();
    installFormatter(wrap(f));
  }

  private static native JavaScriptObject wrap(Formatter formatter) /*-{
    var f = {};
    f.header = function (obj) {
      return formatter.@Formatter::header(Lcom/google/gwt/debugformat/client/Any;)(obj);
    }
    f.hasBody = function (obj) {
      return formatter.@Formatter::hasBody(Lcom/google/gwt/debugformat/client/Any;)(obj);
    }
    f.body = function (obj) {
      return formatter.@Formatter::body(Lcom/google/gwt/debugformat/client/Any;)(obj);
    }
    return f;
  }-*/;

  private static native void installFormatter(JavaScriptObject obj) /*-{
    $wnd.devtoolsFormatter = obj;
  }-*/;
}
