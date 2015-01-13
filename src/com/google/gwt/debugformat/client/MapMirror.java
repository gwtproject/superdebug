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
  public boolean hasBody(Any any) {
    Map m = (Map) any.toJava();
    assert m != null;
    return !m.isEmpty();
  }

  @Override
  public Slice getBody(Any any) {
    Map m = (Map) any.toJava();

    Children out = Children.create();
    out.addInt("size", m.size());
    out.add("entries", makeEntries(m));
    out.add("other fields", any.getJavaFields());
    return out.firstPage();
  }

  private static Slice makeEntries(Map map) {
    // It would be nice to sort the list, but we can only do that if the keys are comparable.
    // (And we don't want to sort a LinkedHashMap.)

    Children out = Children.create();

    for (Object o : map.entrySet()) {
      Map.Entry e = (Map.Entry) o;
      Object key = e.getKey();
      String keyName = key == null ? "null" : key.toString();
      out.add(keyName, e.getValue());
    }

    return out.firstPage();
  }
}
