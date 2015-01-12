package com.google.gwt.debugformat.client;

import com.google.gwt.core.shared.GWT;

import java.util.ArrayList;
import java.util.List;

import static com.google.gwt.debugformat.client.Mirror.Child;

/**
 * Provides custom formats by trying each mirror in turn.
 */
class MirrorFormatter implements Formatter {
  private final List<Mirror> mirrors;

  MirrorFormatter(List<Mirror> mirrors) {
    List<Mirror> m = new ArrayList<>();
    m.add(new PageMirror());
    m.addAll(mirrors);
    this.mirrors = m;
  }

  @Override
  public TemplateNode header(Any object) {
    try {
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
    for (Mirror mirror : mirrors) {
      if (mirror.canDisplay(object)) {
        return mirror.hasChildren(object);
      }
    }
    return false;
  }

  @Override
  public TemplateNode body(Any object) {
    try {
      for (Mirror m : mirrors) {
        if (m.canDisplay(object)) {
          Mirror.Page first = m.getChildren(object);
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
    boolean hasChildren(Any any) {
      return true;
    }

    @Override
    Page getChildren(Any any) {
      return (Page) any.toObject();
    }
  }
}
