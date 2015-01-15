package com.google.gwt.debugformat.client;

import java.util.Collection;
import java.util.Iterator;

/**
 * Provides a custom format for subclasses of Collection that shows their values.
 */
public class CollectionMirror extends IterMirror {
  @Override
  boolean canDisplay(Any any) {
    return any.toJava() instanceof Collection;
  }

  @Override
  Iterator getIterator(Any any) {
    Collection c = (Collection) any.toJava();
    return c.iterator();
  }

  @Override
  int getSize(Any any) {
    Collection c = (Collection) any.toJava();
    return c.size();
  }
}
