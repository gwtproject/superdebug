package com.google.gwt.debugformat.client;

public class NumberMirror extends Mirror {
  @Override
  boolean canDisplay(Any any) {
    return any.toJava() instanceof Number;
  }

  @Override
  String getShortName(Any any) {
    return any.toJava().toString();
  }

  @Override
  String getHeader(Context ctx, Any any) {
    return any.toJava().toString() + " - " + any.getShortJavaClassName();
  }
}
