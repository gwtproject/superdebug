package com.google.gwt.debugformat.client;

/**
 * A range over a list of Children.
 * Used to split up large lists of children in the debugger.
 */
class Slice {
  private final Children children;
  private final int start;
  private final int end; // not included in range

  /**
   * Creates a slice that shows the whole list.
   */
  Slice(Children children) {
    this(children, 0, children.length());
  }

  /**
   * Creates a slice for a range of values.
   */
  Slice(Children children, int start, int end) {
    assert end > start;
    assert start >= 0;
    assert end <= children.length();

    this.children = children;
    this.start = start;
    this.end = end;
  }

  int length() {
    return end - start;
  }

  Mirror.Child get(int index) {
    return children.get(index + start);
  }
}
