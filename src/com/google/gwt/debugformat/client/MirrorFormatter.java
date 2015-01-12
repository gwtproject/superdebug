package com.google.gwt.debugformat.client;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.shared.GWT;

import static com.google.gwt.debugformat.client.Mirror.Child;

/**
 * Provides custom formats by trying each mirror in turn.
 */
class MirrorFormatter implements Formatter {
  private final Iterable<Mirror> mirrors;

  MirrorFormatter(Iterable<Mirror> mirrors) {
    this.mirrors = mirrors;
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
          JsArray<Child> children = m.getChildren(object);
          return renderFields(children);
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

  private static TemplateNode renderFields(JsArray<Child> fields) {
    TemplateBuilder out = new TemplateBuilder("ol");
    out.style("list-style-type:none; padding-left: 0px; margin-top: 0px; margin-bottom: 0px; margin-left: 12px");

    for (int i = 0; i < fields.length(); i++) {
      Child f = fields.get(i);
      Any value = f.getValue();

      out.startTag("li");

      if (value != null && "object".equals(value.typeof())) {
        out.startObjectRef(value);
        nameSpan(out, f);
        if (value.toJava() == null) {
          out.text(value.getTypeName());
        }
        out.endTag();
      } else {
        out.style("padding-left: 13px;");
        out.startTag("span");
        nameSpan(out, f);
        out.text(Any.asString(value));
        out.endTag();
      }

      out.endTag();
    }

    return out.build();
  }

  private static void nameSpan(TemplateBuilder out, Child f) {
    out.startTag("span", "color: rgb(136, 19, 145)");
    out.text(f.getName() + ": ");
    out.endTag();
  }
}
