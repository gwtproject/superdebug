package com.google.gwt.debugformat.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

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
   * A list of the children of a node in the debugger.
   */
  static class Children extends JsArray<Child> {
    protected Children() {}

    static native Children create() /*-{
      return [];
    }-*/;

    final void add(String name, Object value) {
      addAny(name, Any.fromJava(value));
    }

    final void addInt(String name, int value) {
      addAny(name, Any.createJsNumber(value));
    }

    final void addAny(String name, Any value) {
      push(Child.create(name, value));
    }

    final native void sort() /*-{
      this.sort(function compare(a, b) {
        if (a.name < b.name) {
          return -1;
        } else if (a.name == b.name) {
          return 0;
        } else {
          return 1;
        }
    });
    }-*/;

    final Slice firstPage() {
      return new Slice(this);
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
