package com.google.gwt.superdebug.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;

import java.util.Arrays;

/**
 * Installs Super Debug.
 */
public class Installer implements EntryPoint {
  @Override
  public void onModuleLoad() {
    Formatter f = new MirrorFormatter(Arrays.asList(
        new NumberMirror(),
        new MapMirror(), new CollectionMirror(), new ArrayMirror(),
        new WidgetCollectionMirror(), new IndexedPanelMirror(),
        new Mirror()
    ));
    installFormatter(wrap(f));
  }

  private static native JavaScriptObject wrap(Formatter formatter) /*-{
    // Make delegate available mostly for debugging.
    var f = {"delegate": formatter};
    f.header = function (obj) {
      return f.delegate.@Formatter::header(*)(obj);
    }
    f.hasBody = function (obj) {
      return f.delegate.@Formatter::hasBody(*)(obj);
    }
    f.body = function (obj) {
      return f.delegate.@Formatter::body(*)(obj);
    }
    return f;
  }-*/;

  private static native void installFormatter(JavaScriptObject obj) /*-{
    $wnd.devtoolsFormatter = obj;
  }-*/;
}
