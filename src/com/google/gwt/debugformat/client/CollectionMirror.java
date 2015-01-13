package com.google.gwt.debugformat.client;

import java.util.Collection;

/**
 * Provides a custom format for subclasses of Collection that shows their values.
 */
public class CollectionMirror extends Mirror {
  @Override
  boolean canDisplay(Any any) {
    return any.toJava() instanceof Collection;
  }

  @Override
  boolean hasBody(Any any) {
    Collection c = (Collection) any.toJava();
    assert c != null;
    return !c.isEmpty();
  }

  @Override
  Slice getBody(Any any) {
    Collection c = (Collection) any.toJava();

    Children out = Children.create();
    out.addInt("size", c.size());
    out.add("entries", makeEntries(c));
    out.add("other fields", any.getJavaFields());
    return out.firstPage();
  }

  private static Slice makeEntries(Collection c) {
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
