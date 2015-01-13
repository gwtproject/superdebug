package com.google.gwt.debugformat.client;

import com.google.gwt.core.client.JavaScriptObject;

import java.util.Collections;
import java.util.List;

/**
 * Provides reflective access to a Java or JavaScript object, for display in the debugger.
 *
 * <p>The default implementation handles Java objects by displaying their fields.
 */
class Mirror {

  /**
   * Returns true if this mirror can display the given object.
   */
  boolean canDisplay(Any any) {
    return any.isJava();
  }

  /**
   * Returns a single-line summary of the object, to be displayed before it's expanded.
   */
  String getHeader(Any any) {
    return any.getJavaClassName() + " (Java)";
  }

  boolean hasBody(Any any) {
    return any.hasJavaFields();
  }

  Slice getBody(Any any) {
    return any.getJavaFields();
  }

  /**
   * Returns other mirrors required to render children of this mirror.
   */
  List<Mirror> childDeps() {
    return Collections.emptyList();
  }

  /**
   * Represents a child node in the debugger.
   */
  static class Child extends JavaScriptObject {
    protected Child() {}

    static native Child create(String name, Any value) /*-{
      return {name: name, value: value};
    }-*/;

    final native String getName() /*-{
      return this.name;
    }-*/;

    final native Any getValue() /*-{
      return this.value;
    }-*/;
  }
}
