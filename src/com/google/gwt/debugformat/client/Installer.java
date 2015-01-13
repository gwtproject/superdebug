package com.google.gwt.debugformat.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;

import java.util.Arrays;

/**
 * Installs the custom formatter.
 */
public class Installer implements EntryPoint {
  @Override
  public void onModuleLoad() {
    //Formatter f = new HelloFormatter();
    Formatter f = new MirrorFormatter(Arrays.asList(
        new NumberMirror(), new MapMirror(), new CollectionMirror(), new Mirror()));
    installFormatter(wrap(f));
  }

  private static native JavaScriptObject wrap(Formatter formatter) /*-{
    // Make delegate available mostly for debugging.
    var f = {"delegate": formatter};
    f.header = function (obj) {
      return f.delegate.@Formatter::header(Lcom/google/gwt/debugformat/client/Any;)(obj);
    }
    f.hasBody = function (obj) {
      return f.delegate.@Formatter::hasBody(Lcom/google/gwt/debugformat/client/Any;)(obj);
    }
    f.body = function (obj) {
      return f.delegate.@Formatter::body(Lcom/google/gwt/debugformat/client/Any;)(obj);
    }
    return f;
  }-*/;

  private static native void installFormatter(JavaScriptObject obj) /*-{
    $wnd.devtoolsFormatter = obj;
  }-*/;
}
