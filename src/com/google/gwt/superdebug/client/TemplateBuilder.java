package com.google.gwt.superdebug.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayMixed;

/**
 * Builds an HTML element (and its children) in JsonML format.
 */
class TemplateBuilder {

  @SuppressWarnings("unchecked")
  private final JsArray<JsArrayMixed> parents = (JsArray) JavaScriptObject.createArray();

  private JsArrayMixed node;

  TemplateBuilder(String tagName) {
    node = createNode(tagName, null);
  }

  /**
   * Sets the style of the current element.
   */
  void style(String value) {
    setNodeStyle(node, value);
  }

  void text(String text) {
    node.push(text);
  }

  void add(TemplateNode child) {
    node.push(child);
  }

  void startTag(String tagName) {
    startTag(tagName, null);
  }

  void startTag(String tagName, String style) {
    JsArrayMixed child = createNode(tagName, style);
    startChild(child);
  }

  void startObjectRef(Any obj) {
    JsArrayMixed child = (JsArrayMixed) (JavaScriptObject) TemplateNode.createObjectRef(obj);
    startChild(child);
  }

  void endTag() {
    assert parents.length() > 0 : "endTag without matching startTag";
    node = (JsArrayMixed) pop(parents);
  }

  TemplateNode build() {
    assert parents.length() == 0 : "startTag without matching endTag";
    return (TemplateNode)((JavaScriptObject)node);
  }

  private void startChild(JsArrayMixed child) {
    node.push(child);
    parents.push(node);
    node = child;
  }

  private static JsArrayMixed createNode(String tagName, String style) {
    JsArrayMixed node = (JsArrayMixed) (JavaScriptObject) TemplateNode.createElement(tagName);
    setNodeStyle(node, style);
    return node;
  }

  private static native void setNodeStyle(JsArrayMixed node, String value) /*-{
    if (value === null) {
      delete node[1]["style"];
    } else {
      node[1]["style"] = value;
    }
  }-*/;

  private static native JavaScriptObject pop(JsArray array) /*-{
    return array.pop();
  }-*/;
}
