package com.google.gwt.superdebug.client;

public class ArrayMirror extends Mirror {

  @Override
  boolean canDisplay(Any any) {
    return any.isJava() && any.getJavaClass().isArray() && !any.getJavaClass().getComponentType().isPrimitive();
  }

  @Override
  String getHeader(Context ctx, Any any) {
    Object[] array = (Object[]) any.toJava();
    String baseName = array.getClass().getComponentType().getSimpleName();

    StringBuilder out = new StringBuilder();
    out.append(baseName);
    out.append("[" + array.length + "]{");

    for (int i = 0; i < 4; i++) {
      if (i >= array.length) {
        out.append("}");
        return out.toString();
      }
      String name = ctx.getShortName(Any.fromJava(array[i]));
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

  @Override
  boolean hasChildren(Any any) {
    Object[] array = (Object[]) any.toJava();
    return array.length > 0;
  }

  @Override
  Children.Slice getChildren(Any any) {
    Object[] array = (Object[]) any.toJava();

    Children out = Children.create();
    out.addSliced(makeEntries(array));
    out.addAll(any.getJavaFields());
    return out.toSlice();
  }

  private static Children makeEntries(Object[] array) {
    Children out = Children.create();

    int i = 0;
    for (Object item : array) {
      String keyName = String.valueOf(i) + ":";
      out.add(keyName, item);
      i++;
    }

    return out;
  }
}
