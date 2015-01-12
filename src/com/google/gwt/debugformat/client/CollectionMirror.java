package com.google.gwt.debugformat.client;

import java.util.Collection;
import java.util.Iterator;

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
    return !c.isEmpty();
  }

  @Override
  Page getChildren(Any any) {

    Children out = Children.create();

    Collection c = (Collection) any.toObject();
    Iterator it = c.iterator();
    int i = 0;
    while (it.hasNext()) {
      Object item = it.next();
      String keyName = String.valueOf(i);
      out.add(keyName, Any.fromObject(item));
      i++;
    }

    return new Page(out, 0);
  }
}
