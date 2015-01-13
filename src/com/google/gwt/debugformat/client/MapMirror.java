package com.google.gwt.debugformat.client;

import java.util.Iterator;
import java.util.Map;

/**
 * Provides a custom format for subclasses of Map that just shows their keys and values.
 */
class MapMirror extends Mirror {

  @Override
  public boolean canDisplay(Any any) {
    return any.toJava() instanceof Map;
  }

  @Override
  String getHeader(Context ctx, Any any) {
    assert any.toJava() instanceof Map;
    Map m = (Map) any.toJava();

    StringBuilder out = new StringBuilder();
    out.append(any.getShortJavaClassName());
    out.append("[");
    out.append(m.size());
    out.append("] {");

    @SuppressWarnings("unchecked")
    Iterator<Map.Entry> it = m.entrySet().iterator();

    for (int i = 0; i < 4; i++) {
      if (!it.hasNext()) {
        out.append("}");
        return out.toString();
      }

      Map.Entry entry = it.next();

      String key = ctx.getShortName(Any.fromJava(entry.getKey()));
      String value = ctx.getShortName(Any.fromJava(entry.getValue()));
      if (key == null || value == null) {
        if (i == 0) {
          out.append("…}");
          return out.toString();
        } else {
          break;
        }
      }

      if (i > 0) {
        out.append(", ");
      }
      out.append(key);
      out.append(" => ");
      out.append(value);
    }

    out.append(", …}");
    return out.toString();
  }

  @Override
  public boolean hasChildren(Any any) {
    Map m = (Map) any.toJava();
    assert m != null;
    return !m.isEmpty();
  }

  @Override
  public Children.Slice getChildren(Any any) {
    Map m = (Map) any.toJava();

    Children out = Children.create();
    out.addSliced(makeEntries(m));
    out.addAll(any.getJavaFields());
    return out.toSlice();
  }

  private static Children makeEntries(Map map) {
    // We shouldn't sort the keys because it might be a LinkedHashMap or the keys might not be Comparable.

    Children out = Children.create();

    for (Object o : map.entrySet()) {
      Map.Entry e = (Map.Entry) o;
      Object key = e.getKey();
      String keyName = key == null ? "null" : key.toString();
      out.add(keyName + ":", e.getValue());
    }

    return out;
  }
}
