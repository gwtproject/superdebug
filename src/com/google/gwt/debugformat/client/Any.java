package com.google.gwt.debugformat.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * A JavaScript object being inspected in the debugger.
 * (It might also be a Java object, but we don't know that yet.)
 */
class Any extends JavaScriptObject {
  protected Any() {}

  /* === constructors and type checks and casts === */

  static native Any fromJava(Object obj) /*-{
    return obj;
  }-*/;

  static native Any createJsNumber(int val) /*-{
    return val;
  }-*/;

  /**
   * Returns true if this is a regular JavaScript object.
   * (Doesn't include primitives, undefined, or null.)
   */
  final native boolean isJsObject() /*-{
    return this && typeof this === "object";
  }-*/;

  /**
   * Returns true if this is a regular Java object.
   * (Doesn't include instances of String and JavaScriptObject.)
   */
  final native boolean isJava() /*-{
    // TODO: also check that it came from the same GWT app?
    return this && typeof this === 'object' && this.getClass$;
  }-*/;

  /**
   * Returns the same object in its normal Java representation, or null if not Java.
   */
  final Object toJava() {
    if (isJava()) {
      return this;
    } else {
      return null;
    }
  }

  /* === reflection as JavaScript === */

  /**
   * Returns the result of the JavaScript typeof operator.
   */
  final native String getJsType() /*-{
    return typeof this;
  }-*/;

  /**
   * Returns a string summarizing this object's JavaScript type, for display in the debugger.
   */
  final native String getJsName() /*-{
    var t = typeof this;
    if (t === null) {
      return "null";
    } else if (t !== "object") {
      return t;
    } else if (this instanceof Array) {
      return "Array[" + this.length + "]"; // only works in the same frame
    } else if (this.constructor && this.constructor.name) {
      return this.constructor.name;
    } else {
      return "Object";
    }
  }-*/;

  /**
   * Coerce to a string in the JavaScript way.
   */
  static native String getJsString(Any any) /*-{
    return '' + any;
  }-*/;

  /* === reflection as Java === */

  /**
   * Returns the full name of the Java class, or null if it's not Java.
   */
  final String getJavaClassName() {
    if (!isJava()) {
      return null;
    }
    return getClass().getName();
  }

  /**
   * Returns true if this is a Java object with at least one field.
   */
  final boolean hasJavaFields() {
    return isJava() && hasJavaFieldsImpl();
  }

  /**
   * Returns the Java fields as name-value pairs.
   */
  final Slice getJavaFields() {
    if (!isJava()) {
      return null;
    }
    Mirror.Children ch = getFieldsImpl();
    ch.sort();
    return ch.firstPage();
  }

  private native boolean hasJavaFieldsImpl() /*-{
    return !!Object.keys(this).length;
  }-*/;

  private native Mirror.Children getFieldsImpl() /*-{

    // TODO: we need a test to ensure this is in sync with JsIncrementalNamer
    function getFieldName(key) {
      if (!key.endsWith("_g$")) {
        return key;
      }
      var len = key.lastIndexOf('_', key.length - 4);
      if (len < 1) {
        return key;
      }
      return key.substring(0, len);
    }

    var keys = Object.keys(this);
    var fields = [];
    for (var i = 0; i < keys.length; ++i) {
      var key = keys[i];
      var field = {};
      field.name = getFieldName(key);
      field.value = this[key];
      fields.push(field);
    }

    return fields;
  }-*/;
}
