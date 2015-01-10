package com.google.gwt.debugformat.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * A custom renderer for objects being inspected in a debugger.
 */
interface Formatter {

  /**
   * Returns a single-line summary of an object being inspected in the debugger,
   * or null if there is no custom format for this object.
   * (If null, a default will be used.)
   */
  TemplateNode header(JavaScriptObject object);

  /**
   * Returns true if the object can be expanded in the debugger.
   */
  boolean hasBody(JavaScriptObject object);

  /**
   * Returns the expanded version of an object being inspected in the debugger.
   */
  TemplateNode body(JavaScriptObject object);
}
