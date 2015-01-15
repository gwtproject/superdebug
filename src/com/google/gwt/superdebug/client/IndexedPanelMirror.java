package com.google.gwt.superdebug.client;

import com.google.gwt.user.client.ui.IndexedPanel;

import java.util.Iterator;

class IndexedPanelMirror extends IndexedMirror {

  @Override
  boolean canDisplay(Any any) {
    return any.isJava() && any.toJava() instanceof IndexedPanel;
  }

  @Override
  Iterator getIterator(Any any) {
    final IndexedPanel panel = (IndexedPanel) any.toJava();
    return new Iterator() {
      int index = 0;

      @Override
      public boolean hasNext() {
        return index < panel.getWidgetCount();
      }

      @Override
      public Object next() {
        return panel.getWidget(index++);
      }

      @Override
      public void remove() {
        throw new RuntimeException("not implemented");
      }
    };
  }

  @Override
  int getSize(Any any) {
    final IndexedPanel panel = (IndexedPanel) any.toJava();
    return panel.getWidgetCount();
  }
}
