package com.google.gwt.debugformat.client;

/**
 * Provides reflective access to a Java or JavaScript object, for display in the debugger as a tree.
 *
 * <p>The default implementation accepts regular Java objects and displays their fields.
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

  /**
   * Returns true if the node in the debugger tree will have children.
   */
  boolean hasChildren(Any any) {
    return any.hasJavaFields();
  }

  /**
   * Calculates the children to be displayed in the debugger tree.
   */
  Children.Slice getChildren(Any any) {
    return any.getJavaFields();
  }
}
