package com.google.gwt.debugformat.client;

import java.util.Map;

/**
 * Provides a custom format for subclasses of Map that just shows their keys and values.
 */
class MapMirror extends Mirror {

  @Override
  public boolean canDisplay(Any any) {
    return any.toJava() instanceof Map;
  }

  @Override
  public boolean hasChildren(Any any) {
    Map m = (Map) any.toJava();
    assert m != null;
    return !m.isEmpty();
  }

  @Override
  public Children.Slice getChildren(Any any) {
    Map m = (Map) any.toJava();

    Children out = Children.create();
    out.addSliced(makeEntries(m));
    out.addAll(any.getJavaFields());
    return out.toSlice();
  }

  private static Children makeEntries(Map map) {
    // We shouldn't sort the keys because it might be a LinkedHashMap or the keys might not be Comparable.

    Children out = Children.create();

    for (Object o : map.entrySet()) {
      Map.Entry e = (Map.Entry) o;
      Object key = e.getKey();
      String keyName = key == null ? "null" : key.toString();
      out.add(keyName + ":", e.getValue());
    }

    return out;
  }
}
