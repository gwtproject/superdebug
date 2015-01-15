package com.google.gwt.superdebug.client;

import java.util.Iterator;

/**
 * Base class for mirrors that contain ordered elements.
 */
abstract class IndexedMirror extends Mirror {
  @Override
  String getHeader(Context ctx, Any any) {
    StringBuilder out = new StringBuilder();
    out.append(any.getJavaClass().getSimpleName());
    out.append("[" + getSize(any) + "] {");

    Iterator it = getIterator(any);
    for (int i = 0; i < 4; i++) {
      if (!it.hasNext()) {
        out.append("}");
        return out.toString();
      }
      String name = ctx.getShortName(Any.fromJava(it.next()));
      if (name == null) {
        if (i == 0) {
          out.append("…}");
          return out.toString();
        }
        break;
      }

      if (i > 0) {
        out.append(", ");
      }
      out.append(name);
    }
    out.append(", …}");
    return out.toString();
  }

  abstract Iterator getIterator(Any any);

  abstract int getSize(Any any);

  @Override
  boolean hasChildren(Any any) {
    return getSize(any) > 0;
  }

  @Override
  Children.Slice getChildren(Any any) {
    Children out = Children.create();
    out.addSliced(makeEntries(any));
    out.addAll(any.getJavaFields());
    return out.toSlice();
  }

  private Children makeEntries(Any any) {
    Children out = Children.create();

    Iterator it = getIterator(any);
    int i = 0;
    while (it.hasNext()) {
      Object item = it.next();
      String keyName = String.valueOf(i) + ":";
      out.add(keyName, item);
      i++;
    }

    return out;
  }
}
