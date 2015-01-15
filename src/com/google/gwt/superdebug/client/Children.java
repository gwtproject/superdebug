package com.google.gwt.superdebug.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * A container for child nodes in the debugger tree.
 */
class Children extends JsArray<Children.Entry> {

  protected Children() {}

  static native Children create() /*-{
    return [];
  }-*/;

  final void add(String name, Object value) {
    addAny(name, Any.fromJava(value));
  }

  final void addAny(String name, Any value) {
    push(Entry.create(name, value));
  }

  final void addAll(Slice slice) {
    for (int i = 0; i < slice.length(); i++) {
      Entry ch = slice.get(i);
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
    return "[" + start + " … " + (end - 1) + "]";
  }

  /**
   * A child in the debugger tree.
   */
  static class Entry extends JavaScriptObject {
    protected Entry() {}

    static native Entry create(String name, Any value) /*-{
      return {name: name, value: value};
    }-*/;

    final native String getName() /*-{
      return this.name;
    }-*/;

    final native Any getValue() /*-{
      return this.value;
    }-*/;
  }

  /**
   * A contiguous subset of Children.
   * Used to split up large lists of children in the debugger.
   */
  static class Slice {
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
      assert end >= start;
      assert start >= 0;
      assert end <= children.length();

      this.children = children;
      this.start = start;
      this.end = end;
    }

    int length() {
      return end - start;
    }

    Entry get(int index) {
      return children.get(index + start);
    }
  }
}
