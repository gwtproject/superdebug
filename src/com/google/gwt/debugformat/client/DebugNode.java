package com.google.gwt.debugformat.client;

/**
 * A fake node in the debug tree.
 * (It doesn't correspond directly to a JavaScript object.)
 */
interface DebugNode {

  String getHeader();

  boolean hasBody();

  Slice getBody();
}
