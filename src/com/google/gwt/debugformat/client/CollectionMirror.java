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
  boolean hasChildren(Any any) {
    Collection c = (Collection) any.toJava();
    assert c != null;
    return !c.isEmpty();
  }

  @Override
  Children.Slice getChildren(Any any) {
    Collection c = (Collection) any.toJava();

    Children out = Children.create();
    out.addSliced(makeEntries(c));
    out.addAll(any.getJavaFields());
    return out.toSlice();
  }

  private static Children makeEntries(Collection c) {
    Children out = Children.create();

    int i = 0;
    for (Object item : c) {
      String keyName = String.valueOf(i) + ":";
      out.add(keyName, item);
      i++;
    }

    return out;
  }
}
