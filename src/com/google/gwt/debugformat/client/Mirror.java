package com.google.gwt.debugformat.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * Provides reflective access to a Java or JavaScript object, for display in the debugger.
 * The default implementation displays if it's a generic Java object.
 */
class Mirror {

  /**
   * Returns true if this mirror can display the given object.
   */
  boolean canDisplay(Any any) {
    return any.toJava() != null;
  }

  /**
   * Returns a single-line summary of the object, to be displayed before it's expanded.
   */
  String getHeader(Any any) {
    return any.toJava().getClassName() + " (Java)";
  }

  boolean hasChildren(Any any) {
    return any.toJava().hasFields();
  }

  Children getChildren(Any any) {
    return any.toJava().getFields();
  }

  /**
   * A list of the children of a node in the debugger.
   */
  static class Children extends JsArray<Child> {
    protected Children() {}

    static native Children create() /*-{
      return [];
    }-*/;

    final void add(String name, Any value) {
      push(Child.create(name, value));
    }
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
