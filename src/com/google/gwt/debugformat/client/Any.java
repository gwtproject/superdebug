package com.google.gwt.debugformat.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * A JavaScript object being inspected in the debugger.
 * (It might also be a Java object, but we don't know that yet.)
 */
class Any extends JavaScriptObject {
  protected Any() {}

  /* === as JavaScript === */

  /**
   * Returns true if this is a regular JavaScript object (including arrays).
   * (Doesn't include primitives, undefined, or null.)
   */
  final native boolean isJsObject() /*-{
    return this && typeof this === "object";
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
  final native String getJsStringValue() /*-{
    if (typeof this === "string") {
      return JSON.stringify(this);
    }
    return '' + this;
  }-*/;

  /* === as Java === */

  static native Any fromJava(Object obj) /*-{
    return obj;
  }-*/;

  /**
   * Returns true if this is a regular Java object or array.
   * (Doesn't include instances of String or JavaScriptObject.)
   */
  final native boolean isJava() /*-{
    // TODO: also check that it came from the same GWT app?
    if (!this || typeof this !== "object") {
      return false;
    }
    if (Array.isArray(this)) {
      return this.___clazz$;
    } else {
      // Check if it's an instance, excluding prototypes.
      return this.getClass$ && !this.hasOwnProperty("___clazz$");
    }
  }-*/;

  /**
   * Returns this object in its normal Java representation, or null if not Java.
   */
  final Object toJava() {
    if (isJava()) {
      return this;
    } else {
      return null;
    }
  }

  final Class getJavaClass() {
    if (!isJava()) {
      return null;
    }
    return getClass();
  }

  /**
   * Returns true if this is a Java object with at least one field.
   */
  final boolean hasJavaFields() {
    return isJava() && hasJavaFieldsImpl();
  }

  /**
   * Returns the Java fields as name-value pairs, or null if not Java.
   */
  final Children.Slice getJavaFields() {
    if (!isJava()) {
      return null;
    }
    Children ch = getFieldsImpl();
    ch.sort();
    ch.add("|class|", new DebugNode(getJavaClass().getCanonicalName(), null));
    return ch.toSlice();
  }

  private native boolean hasJavaFieldsImpl() /*-{
    return !!Object.keys(this).length;
  }-*/;

  private native Children getFieldsImpl() /*-{

    // TODO: we need a test to ensure this is in sync with JsIncrementalNamer
    function getJavaFieldName(key) {
      if (!key.endsWith("_g$")) {
        return null;
      }
      var len = key.lastIndexOf('_', key.length - 4);
      if (len < 1) {
        return null;
      }
      return key.substring(0, len);
    }

    function isField(key, name, value) {
      if (name === "$init" || name === "$H") {
        return false;
      }
      if (typeof value === "function") { // } && !Object.hasOwnProperty(key)) {
        return false;
      }
      return true;
    }

    var javaFields = [];
    // Deliberately looping over inherited props
    for (var key in this) {
      if (typeof key === "string") {
        var name = getJavaFieldName(key);
        var value = this[key];
        if (name && isField(key, name, value)) {
          var longValue = @Any::toLongValue(Lcom/google/gwt/debugformat/client/Any;)(value);
          if (longValue !== null) {
            value = longValue;
          }
          var child = {
            name: name + ":",
            value: value
          };
          javaFields.push(child);
        }
      }
    }
    return javaFields;
  }-*/;

  // TODO: better way to convert primitive longs to a string? Maybe use @UnsafeNativeLong annotation?

  /**
   * If this is an unboxed Java long, returns its value as a DebugNode. Otherwise returns null.
   * @return
   */
  private static DebugNode toLongValue(Any any) {
    if (!isLong(any)) {
      return null;
    }
    Long result = new Long(987654321);
    if (any.toLongValueImpl(result)) {
      return new DebugNode(result.toString(), null);
    } else {
      return null;
    }
  }

  private native boolean toLongValueImpl(Long target) /*-{
    var keys = Object.keys(target);
    for (var i = 0; i < keys.length; i++) {
      var key = keys[i];
      if (key.startsWith("value") && key.endsWith("_g$")) {
        if (@Any::isLong(Lcom/google/gwt/debugformat/client/Any;)(target[key])) {
          target[key] = this;
          return true;
        }
      }
    }
    return false; // not found
  }-*/;

  /**
   * Returns true if this is an unboxed Java long.
   */
  private static native boolean isLong(Any x) /*-{
    return x && typeof x === "object" && x.constructor && x.constructor.name === "Object" &&
        Object.keys(x).length === 3 &&
        typeof x.h === "number" && typeof x.m === "number" && typeof x.l === "number";
  }-*/;
}
