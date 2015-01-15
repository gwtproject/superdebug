package com.google.gwt.superdebug.client;

import com.google.gwt.core.shared.GWT;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * Provides custom formats by trying each mirror in turn.
 */
class MirrorFormatter implements Formatter, Mirror.Context {
  private final LinkedHashSet<Mirror> mirrors;

  MirrorFormatter(List<Mirror> mirrors) {
    LinkedHashSet<Mirror> ms = new LinkedHashSet<>();
    ms.add(new DebugNodeMirror());
    ms.addAll(mirrors);
    this.mirrors = ms;
  }

  @Override
  public String getShortName(Any any) {
    if (any.isJsObject()) {
      for (Mirror m : mirrors) {
        if (m.canDisplay(any)) {
          String shortName = m.getShortName(any);
          if (shortName != null && shortName.length() <= Mirror.MAX_SHORT_NAME) {
            return shortName;
          }
          return null;
        }
      }
    } else {
      String shortName = any.getJsStringValue();
      if (shortName.length() <= Mirror.MAX_SHORT_NAME) {
        return shortName;
      }
    }
    return null;
  }

  @Override
  public TemplateNode header(Any any) {
    try {
      if (any.isJsObject()) {
        for (Mirror m : mirrors) {
          if (m.canDisplay(any)) {
            String header = m.getHeader(this, any);
            return renderHeader(header);
          }
        }
      }
      return null;
    } catch (Exception e) {
      GWT.log("error while formatting header for Java object", e);
      return null;
    }
  }

  @Override
  public boolean hasBody(Any any) {
    try {
      for (Mirror mirror : mirrors) {
        if (mirror.canDisplay(any)) {
          return mirror.hasChildren(any);
        }
      }
      return false;
    } catch (Exception e) {
      GWT.log("error while formatting Java object", e);
      return false;
    }
  }

  @Override
  public TemplateNode body(Any any) {
    try {
      for (Mirror m : mirrors) {
        if (m.canDisplay(any)) {
          return renderBody(m.getChildren(any));
        }
      }
      return null;
    } catch (Exception e) {
      GWT.log("error while formatting body for Java object", e);
      return null;
    }
  }

  boolean isHeaderOnly(Any any) {
    if (any.isJsObject()) {
      for (Mirror m : mirrors) {
        if (m.canDisplay(any)) {
          return !m.hasChildren(any);
        }
      }
    }
    return false;
  }

  private static TemplateNode renderHeader(String header) {
    TemplateBuilder b = new TemplateBuilder("span");
    b.text(header);
    return b.build();
  }

  TemplateNode renderBody(Children.Slice children) {
    TemplateBuilder out = new TemplateBuilder("ol");
    out.style("list-style-type:none; padding-left: 0px; margin-top: 0px; margin-bottom: 0px; margin-left: 12px");

    for (int i = 0; i < children.length(); i++) {
      renderChild(out, children.get(i));
    }

    return out.build();
  }

  private void renderChild(TemplateBuilder out, Children.Entry entry) {
    Any value = entry.getValue();

    out.startTag("li");

    if (isHeaderOnly(value)) {
      out.style("padding-left: 13px;");
      out.startTag("span");
      nameSpan(out, entry.getName());
      out.add(header(value));
      out.endTag();
    } else if (value.isJsObject()) {
      out.startObjectRef(value);
      nameSpan(out, entry.getName());
      if (!value.isJava()) {
        // The header isn't automatically generated for non-custom objects.
        out.text(value.getJsName());
      }
      out.endTag();
    } else {
      out.style("padding-left: 13px;");
      out.startTag("span");
      nameSpan(out, entry.getName());
      out.text(value.getJsStringValue());
      out.endTag();
    }

    out.endTag();
  }

  private static void nameSpan(TemplateBuilder out, String name) {
    if (name.isEmpty()) {
      return;
    }
    out.startTag("span", "color: rgb(136, 19, 145)");
    out.text(name + " ");
    out.endTag();
  }

  private static class DebugNodeMirror extends Mirror {
    @Override
    boolean canDisplay(Any any) {
      return any.toJava() instanceof DebugNode;
    }

    @Override
    String getHeader(Context ctx, Any any) {
      DebugNode n = (DebugNode) any.toJava();
      return n.getHeader(ctx);
    }

    @Override
    boolean hasChildren(Any any) {
      DebugNode n = (DebugNode) any.toJava();
      return n.hasBody();
    }

    @Override
    Children.Slice getChildren(Any any) {
      DebugNode n = (DebugNode) any.toJava();
      return n.getBody();
    }
  }
}
