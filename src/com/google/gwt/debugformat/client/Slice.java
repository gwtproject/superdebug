package com.google.gwt.debugformat.client;

/**
 * A range within a list of Children.
 * Used to page through large lists of children.
 */
class Slice implements DebugNode {
  // The number of children to display before a "More" prompt.
  static final int CHILDREN_PER_PAGE = 100;

  private final Mirror.Children children;
  private final int start;
  private final int end; // not included in range

  /**
   * Creates a slice for the first page.
   */
  Slice(Mirror.Children children) {
    assert children.length() > 0;
    this.children = children;
    this.start = 0;
    this.end = children.length() > CHILDREN_PER_PAGE ? CHILDREN_PER_PAGE : children.length();
  }

  private Slice(Mirror.Children children, int start, int end) {
    assert end > start;
    assert end <= children.length();

    this.children = children;
    this.start = start;
    this.end = end;
  }

  @Override
  public String getHeader() {
    if (start == 0 && end == children.length()) {
      return "";
    }
    int lastIndex = start + length() - 1;
    return "[" + start + "..." + lastIndex + "]";
  }

  @Override
  public boolean hasBody() {
    return true;
  }

  @Override
  public Slice getBody() {
    return this;
  }

  TemplateNode renderAsBody() {
    TemplateBuilder out = new TemplateBuilder("ol");
    out.style("list-style-type:none; padding-left: 0px; margin-top: 0px; margin-bottom: 0px; margin-left: 12px");

    for (int i = 0; i < length(); i++) {
      Mirror.Child f = get(i);
      Any value = f.getValue();

      out.startTag("li");

      if (value != null && "object".equals(value.getJsType())) {
        out.startObjectRef(value);
        nameSpan(out, f.getName());
        if (!value.isJava()) {
          out.text(value.getJsName());
        }
        out.endTag();
      } else {
        out.style("padding-left: 13px;");
        out.startTag("span");
        nameSpan(out, f.getName());
        out.text(Any.getJsString(value));
        out.endTag();
      }

      out.endTag();
    }

    Slice next = nextPage();
    if (next != null) {
      out.startObjectRef(Any.fromJava(next));
      nameSpan(out, "(More)");
      out.endTag();
    }

    return out.build();
  }

  Slice nextPage() {
    int nextEnd = end + CHILDREN_PER_PAGE;
    if (nextEnd >= children.length()) {
      nextEnd = children.length();
    }
    if (nextEnd <= end) {
      return null;
    }
    return new Slice(children, end, nextEnd);
  }

  private int length() {
    return end - start;
  }

  private Mirror.Child get(int index) {
    return children.get(index + start);
  }

  private static void nameSpan(TemplateBuilder out, String name) {
    out.startTag("span", "color: rgb(136, 19, 145)");
    out.text(name + ": ");
    out.endTag();
  }
}
