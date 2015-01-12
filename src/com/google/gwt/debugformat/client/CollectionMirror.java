package com.google.gwt.debugformat.client;

import java.util.Collection;

/**
 * Provides a custom format for subclasses of Collection that shows their values.
 */
public class CollectionMirror extends Mirror {
  @Override
  boolean canDisplay(Any any) {
    return any.toObject() instanceof Collection;
  }

  @Override
  boolean hasChildren(Any any) {
    Collection c = (Collection) any.toObject();
    assert c != null;
    return !c.isEmpty();
  }

  @Override
  Page getChildren(Any any) {
    Collection c = (Collection) any.toObject();

    Children out = Children.create();
    out.add("size", Any.fromInt(c.size()));
    out.add("entries", Any.fromObject(makeEntries(c)));
    return out.firstPage();
  }

  private static Page makeEntries(Collection c) {
    Children out = Children.create();

    int i = 0;
    for (Object item : c) {
      String keyName = String.valueOf(i);
      out.add(keyName, Any.fromObject(item));
      i++;
    }
    return out.firstPage();
  }
}
