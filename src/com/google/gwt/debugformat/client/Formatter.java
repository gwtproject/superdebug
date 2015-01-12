package com.google.gwt.debugformat.client;

/**
 * A custom renderer for objects being inspected in a debugger.
 */
interface Formatter {

  /**
   * Returns a single-line summary of an object being inspected in the debugger,
   * or null if there is no custom format for this object.
   * (If null, a default will be used.)
   */
  TemplateNode header(Any object);

  /**
   * Returns true if the object can be expanded in the debugger.
   */
  boolean hasBody(Any object);

  /**
   * Returns the expanded version of an object being inspected in the debugger.
   */
  TemplateNode body(Any object);
}
