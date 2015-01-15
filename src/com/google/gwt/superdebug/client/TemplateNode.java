package com.google.gwt.superdebug.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * A node in an HTML template.
 * The internal representation is a subset of JsonML format.
 * @see TemplateBuilder
 */
class TemplateNode extends JavaScriptObject {
  protected TemplateNode() {}

  /**
   * Create a node representing an HTML element.
   * These tags are allowed: div, span, ol, li, table, tr, td.
   * (This is not checked here, but DevTools won't render a template containing any others.)
   */
  static native TemplateNode createElement(String tag) /*-{
    // Work around a bug in Devtools.
    // It expects the array's constructor to belong to the top-level window.
    // Reported as a comment on: https://codereview.chromium.org/774903003/
    return new $wnd.Array(tag, {});
  }-*/;

  /**
   * Creates a reference to a child object that should be substituted into the template.
   * (The debugger will render it as an inspectable child object.)
   */
  static native TemplateNode createObjectRef(Any obj) /*-{
    return new $wnd.Array("object", {"object": obj});
  }-*/;

}
