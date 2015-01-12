package com.google.gwt.debugformat.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * A JavaScript object being inspected in the debugger.
 * (It might be a Java object, but we don't know that yet.)
 */
class Any extends JavaScriptObject {
  protected Any() {}

  static native Any fromObject(Object obj) /*-{
    return obj;
  }-*/;

  static native Any fromInt(int val) /*-{
    return val;
  }-*/;

  /**
   * Returns the result of the JavaScript typeof operator.
   */
  final native String typeof() /*-{
    return typeof this;
  }-*/;

  /**
   * Returns a string summarizing this object's JavaScript type, for display in the debugger.
   */
  final native String getTypeName() /*-{
    var t = typeof this;
    if (t === null) {
      return "null";
    } else if (t !== "object") {
      return t;
    } else if (this.constructor && this.constructor.name) {
      return this.constructor.name;
    } else {
      return "Object";
    }
  }-*/;

  /**
   * Coerce to a string in the JavaScript way.
   */
  static native String asString(Any any) /*-{
    return '' + any;
  }-*/;

  /**
   * Returns the Java object, or null if not Java.
   */
  final native JavaObject toJava() /*-{
    // TODO: also check that it came from the same GWT app?
    if (typeof this === 'object' && this !== null && this.getClass$) {
      return this;
    } else {
      return null;
    }
  }-*/;

  /**
   * Returns the same object in its normal Java representation, or null if not Java.
   */
  final Object toObject() {
    return toJava();
  }
}
