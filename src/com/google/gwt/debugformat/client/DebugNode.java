package com.google.gwt.debugformat.client;

/**
 * A fake node in the debug tree.
 * (It doesn't correspond directly to a JavaScript object.)
 */
class DebugNode {
  final String header;
  final Slice body;

  DebugNode(String header, Slice body) {
    this.header = header;
    this.body = body;
  }

  String getHeader() {
    return header;
  }

  boolean hasBody() {
    return body != null;
  }

  Slice getBody() {
    return body;
  }
}
