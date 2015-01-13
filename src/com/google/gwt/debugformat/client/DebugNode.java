package com.google.gwt.debugformat.client;

/**
 * A fake node in the debugger tree.
 * (It need not correspond directly to a JavaScript object.)
 */
class DebugNode {
  final String header;
  final Children.Slice body;

  DebugNode(String header, Children.Slice body) {
    this.header = header;
    this.body = body;
  }

  String getHeader(Mirror.Context ctx) {
    return header;
  }

  boolean hasBody() {
    return body != null;
  }

  Children.Slice getBody() {
    return body;
  }
}
