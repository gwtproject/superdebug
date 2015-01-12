package com.google.gwt.debugformat.client;

import java.util.Iterator;
import java.util.Map;

class MapMirror extends Mirror {
  // The number of children to display before a "More" prompt.
  private static final int PAGE_SIZE = 1000;

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

    Map m = (Map) any.toObject();
    Iterator it = m.entrySet().iterator();

    Children out = Children.create();

    int i = 0;
    while (it.hasNext()) {
      if (i > PAGE_SIZE) {
        break; // TODO: more button
      }
      Map.Entry e = (Map.Entry) it.next();
      Object key = e.getKey();
      out.add(key == null ? "null" : key.toString(), Any.fromObject(e.getValue()));
      i++;
    }

    return out;
  }
}
