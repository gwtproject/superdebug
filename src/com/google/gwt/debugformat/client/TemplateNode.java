package com.google.gwt.debugformat.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * A node in an HTML template.
 * The internal representation is a subset of JsonML format.
 * @see TemplateBuilder
 */
class TemplateNode extends JavaScriptObject {
  protected TemplateNode() {}

  /**
   * Creates a plain text node.
   */
  static native TemplateNode fromText(String text) /*-{
    return text;
  }-*/;

  /**
   * Creates a reference to a child object. The debugger will render it (recursively).
   */
  static native TemplateNode fromObject(Object obj) /*-{
    return ["object", {"object": obj}];
  }-*/;

}
