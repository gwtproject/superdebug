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
  boolean hasBody(Any any) {
    Collection c = (Collection) any.toObject();
    assert c != null;
    return !c.isEmpty();
  }

  @Override
  Page getBody(Any any) {
    Collection c = (Collection) any.toObject();

    Children out = Children.create();
    out.addInt("size", c.size());
    out.add("entries", makeEntries(c));
    return out.firstPage();
  }

  private static Page makeEntries(Collection c) {
    Children out = Children.create();

    int i = 0;
    for (Object item : c) {
      String keyName = String.valueOf(i);
      out.add(keyName, item);
      i++;
    }

    return out.firstPage();
  }
}
