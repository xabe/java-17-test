package com.xabe.visitor;

import java.util.List;
import java.util.stream.Stream;

public final class Car implements CarElement {

  private final List<CarElement> elements;

  public Car() {
    this.elements = List.of(new Body(), new Engine());
  }

  public Stream<CarElement> elements() {
    return Stream.concat(this.elements.stream(), Stream.of(this));
  }

}
