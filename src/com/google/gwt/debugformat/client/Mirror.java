package com.google.gwt.debugformat.client;

/**
 * Provides reflective access to a Java or JavaScript object, for display in the debugger as a tree.
 *
 * <p>The default implementation accepts regular Java objects and displays their fields.
 */
class Mirror {
  static final int MAX_SHORT_NAME = 10;

  /**
   * Returns true if this mirror can display the given object.
   */
  boolean canDisplay(Any any) {
    return any.isJava();
  }

  /**
   * Returns a short name that can be used in a header, or null if not available.
   * The name will only be used if it's less than {@link #MAX_SHORT_NAME} characters.
   */
  String getShortName(Any any) {
    return null;
  }

  /**
   * Returns a single-line summary of the object, to be displayed before it's expanded.
   */
  String getHeader(Context ctx, Any any) {
    return any.getShortJavaClassName() + " (Java)";
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

  interface Context {

    /**
     * Returns a short name (not more than 20 characters or so) that can be used in a header,
     * or null if not available.
     *
     * <p>This will poll all the mirrors.
     */
    String getShortName(Any any);
  }
}
