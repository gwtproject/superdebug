package com.google.gwt.debugformat.client;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.shared.GWT;

import static com.google.gwt.debugformat.client.JavaObject.Field;

/**
 * Provides a custom format for generic Java objects.
 */
public class JavaFormatter implements Formatter {
  @Override
  public TemplateNode header(Any object) {

    JavaObject obj = object.toJava();
    if (obj == null) {
      return null;
    }

    try {
      return javaHeader(obj);
    } catch (Exception e) {
      GWT.log("error while formatting header for Java object", e);
      return null;
    }
  }

  private TemplateNode javaHeader(JavaObject obj) {
    TemplateBuilder b = new TemplateBuilder("span");
    b.text(obj.getClassName() + " (Java)");
    return b.build();
  }

  @Override
  public boolean hasBody(Any object) {
    JavaObject obj = object.toJava();
    return obj != null && obj.hasFields();
  }

  @Override
  public TemplateNode body(Any object) {
    JavaObject obj = object.toJava();
    if (obj == null) {
      return null;
    }
    try {
      return renderFields(obj.getFields());
    } catch (Exception e) {
      GWT.log("error while formatting body for Java object", e);
      return null;
    }
  }

  private TemplateNode renderFields(JsArray<Field> fields) {
    TemplateBuilder out = new TemplateBuilder("ol");
    out.style("list-style-type:none; padding-left: 0px; margin-top: 0px; margin-bottom: 0px; margin-left: 12px");

    for (int i = 0; i < fields.length(); i++) {
      Field f = fields.get(i);
      Any value = f.getValue();

      out.startTag("li");

      if (value != null && "object".equals(value.typeof())) {
        out.startObjectRef(value);
        nameSpan(out, f);
        if (value.toJava() == null) {
          out.text(value.getTypeName());
        }
        out.endTag();
      } else {
        out.style("padding-left: 13px;");
        out.startTag("span");
        nameSpan(out, f);
        out.text(Any.asString(value));
        out.endTag();
      }

      out.endTag();
    }

    return out.build();
  }

  private void nameSpan(TemplateBuilder out, Field f) {
    out.startTag("span", "color: rgb(136, 19, 145)");
    out.text(f.getName() + ": ");
    out.endTag();
  }
}
