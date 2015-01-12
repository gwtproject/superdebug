package com.google.gwt.debugformat.client;

import com.google.gwt.core.shared.GWT;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static com.google.gwt.debugformat.client.Mirror.Child;

/**
 * Provides custom formats by trying each mirror in turn.
 */
class MirrorFormatter implements Formatter {
  private final LinkedHashSet<Mirror> mirrors;

  MirrorFormatter(List<Mirror> mirrors) {
    LinkedHashSet<Mirror> out = new LinkedHashSet<>();
    out.add(new PageMirror());

    // Find all the mirrors and their dependencies.
    // (Breadth-first for no particular reason.)
    Queue<Mirror> toVisit = new LinkedList<>(mirrors);
    while (!toVisit.isEmpty()) {
      Mirror m = toVisit.remove();
      out.add(m);
      toVisit.addAll(m.childDeps());
    }
    this.mirrors = out;
  }

  @Override
  public TemplateNode header(Any object) {
    try {
      if ("object".equals(object.typeof()))
      for (Mirror m : mirrors) {
        if (m.canDisplay(object)) {
          String header = m.getHeader(object);
          return renderHeader(header);
        }
      }
      return null;
    } catch (Exception e) {
      GWT.log("error while formatting header for Java object", e);
      return null;
    }
  }

  @Override
  public boolean hasBody(Any object) {
    try {
      for (Mirror mirror : mirrors) {
        if (mirror.canDisplay(object)) {
          return mirror.hasBody(object);
        }
      }
      return false;
    } catch (Exception e) {
      GWT.log("error while formatting Java object", e);
      return false;
    }
  }

  @Override
  public TemplateNode body(Any object) {
    try {
      for (Mirror m : mirrors) {
        if (m.canDisplay(object)) {
          Mirror.Page first = m.getBody(object);
          return renderFields(first);
        }
      }
      return null;
    } catch (Exception e) {
      GWT.log("error while formatting body for Java object", e);
      return null;
    }
  }

  private static TemplateNode renderHeader(String header) {
    TemplateBuilder b = new TemplateBuilder("span");
    b.text(header);
    return b.build();
  }

  private static TemplateNode renderFields(Mirror.Page p) {
    TemplateBuilder out = new TemplateBuilder("ol");
    out.style("list-style-type:none; padding-left: 0px; margin-top: 0px; margin-bottom: 0px; margin-left: 12px");

    for (int i = 0; i < p.length(); i++) {
      Child f = p.get(i);
      Any value = f.getValue();

      out.startTag("li");

      if (value != null && "object".equals(value.typeof())) {
        out.startObjectRef(value);
        nameSpan(out, f.getName());
        if (value.toJava() == null) {
          out.text(value.getTypeName());
        }
        out.endTag();
      } else {
        out.style("padding-left: 13px;");
        out.startTag("span");
        nameSpan(out, f.getName());
        out.text(Any.asString(value));
        out.endTag();
      }

      out.endTag();
    }

    Mirror.Page next = p.nextPage();
    if (next != null) {
      out.startObjectRef(Any.fromObject(next));
      nameSpan(out, "(More)");
      out.endTag();
    }

    return out.build();
  }

  private static void nameSpan(TemplateBuilder out, String name) {
    out.startTag("span", "color: rgb(136, 19, 145)");
    out.text(name + ": ");
    out.endTag();
  }

  private static class PageMirror extends Mirror {
    @Override
    boolean canDisplay(Any any) {
      return any.toObject() instanceof Page;
    }

    @Override
    String getHeader(Any any) {
      Page p = (Page) any.toObject();
      return "[" + p.firstIndex() + "..." + p.lastIndex() + "]";
    }

    @Override
    boolean hasBody(Any any) {
      return true;
    }

    @Override
    Page getBody(Any any) {
      return (Page) any.toObject();
    }
  }
}
