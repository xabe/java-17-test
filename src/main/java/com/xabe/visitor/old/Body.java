package com.xabe.visitor.old;

public class Body implements CarElement {

  @Override
  public void accept(final CarElementVisitor visitor) {
    visitor.visit(this);
  }
}
