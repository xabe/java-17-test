package com.xabe.visitor.old;

public class Engine implements CarElement {

  @Override
  public void accept(final CarElementVisitor visitor) {
    visitor.visit(this);
  }
}
