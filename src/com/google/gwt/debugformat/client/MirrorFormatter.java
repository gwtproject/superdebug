package com.google.gwt.debugformat.client;

import com.google.gwt.core.shared.GWT;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Provides custom formats by trying each mirror in turn.
 */
class MirrorFormatter implements Formatter {
  private final LinkedHashSet<Mirror> mirrors;

  MirrorFormatter(List<Mirror> mirrors) {
    LinkedHashSet<Mirror> out = new LinkedHashSet<>();
    out.add(new DebugNodeMirror());

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
  public TemplateNode header(Any any) {
    try {
      if (any.isJsObject()) {
        for (Mirror m : mirrors) {
          if (m.canDisplay(any)) {
            String header = m.getHeader(any);
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
          Slice first = m.getBody(object);
          return first.renderAsBody();
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

  private static class DebugNodeMirror extends Mirror {
    @Override
    boolean canDisplay(Any any) {
      return any.toJava() instanceof DebugNode;
    }

    @Override
    String getHeader(Any any) {
      DebugNode n = (DebugNode) any.toJava();
      return n.getHeader();
    }

    @Override
    boolean hasBody(Any any) {
      DebugNode n = (DebugNode) any.toJava();
      return n.hasBody();
    }

    @Override
    Slice getBody(Any any) {
      DebugNode n = (DebugNode) any.toJava();
      return n.getBody();
    }
  }
}
