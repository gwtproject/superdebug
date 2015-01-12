package com.google.gwt.debugformat.client;

import java.util.Iterator;
import java.util.Map;

/**
 * Provides a custom format for subclasses of Map that just shows their keys and values.
 */
class MapMirror extends Mirror {

  @Override
  public boolean canDisplay(Any any) {
    return any.toObject() instanceof Map;
  }

  @Override
  public boolean hasChildren(Any any) {
    Map m = (Map) any.toObject();
    return !m.isEmpty();
  }

  @Override
  public Children getChildren(Any any) {

    // It would be nice to sort the list, but we can only do that if the keys are comparable.
    // (And we don't want to sort a LinkedHashMap.)

    Children out = Children.create();

    Map m = (Map) any.toObject();
    Iterator it = m.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry e = (Map.Entry) it.next();
      Object key = e.getKey();
      String keyName = key == null ? "null" : key.toString();
      out.add(keyName, Any.fromObject(e.getValue()));
    }

    return out;
  }
}
