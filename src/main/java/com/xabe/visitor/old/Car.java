package com.xabe.visitor.old;

import java.util.List;

public class Car implements CarElement {

  private final List<CarElement> elements;

  public Car() {
    this.elements = List.of(new Body(), new Engine());
  }

  @Override
  public void accept(final CarElementVisitor visitor) {
    for (final CarElement element : this.elements) {
      element.accept(visitor);
    }
    visitor.visit(this);
  }


}
