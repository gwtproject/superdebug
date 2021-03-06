package com.google.gwt.superdebug.client;

class NumberMirror extends Mirror {
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
    return any.toJava().toString() + " (" + any.getJavaClass().getSimpleName() + ")";
  }
}
