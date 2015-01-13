package com.google.gwt.debugformat.client;

import com.google.gwt.core.client.JsArray;

/**
 * Contains child fields for a node in the debugger tree.
 */
class Children extends JsArray<Mirror.Child> {

  protected Children() {}

  static native Children create() /*-{
    return [];
  }-*/;

  final void add(String name, Object value) {
    addAny(name, Any.fromJava(value));
  }

  final void addAny(String name, Any value) {
    push(Mirror.Child.create(name, value));
  }

  final void addAll(Slice slice) {
    for (int i = 0; i < slice.length(); i++) {
      Mirror.Child ch = slice.get(i);
      addAny(ch.getName(), ch.getValue());
    }
  }

  /**
   * Adds the given children to this list, possibly grouped into slices.
   */
  final void addSliced(Children entries) {
    add("<entries>[" + entries.length() + "]", new DebugNode("", entries.slice(100)));
  }

  private Slice slice(int maxPerSlice) {
    if (length() <= maxPerSlice) {
      return this.toSlice();
    }

    Children out = Children.create();

    int start = 0;
    while (true) {
      int end = start + maxPerSlice;
      if (end >= length()) {
        Slice slice = new Slice(this, start, length());
        out.add(getRangeName(start, length()), new DebugNode("", slice));
        break;
      }
      Slice slice = new Slice(this, start, end);
      out.add(getRangeName(start, end), new DebugNode("", slice));
      start = end;
    }

    return out.toSlice();
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

  final Slice toSlice() {
    return new Slice(this);
  }

  private static String getRangeName(int start, int end) {
    return "[" + start + " \u2026 " + (end - 1) + "]";
  }
}
