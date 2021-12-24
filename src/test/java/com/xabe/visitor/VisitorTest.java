package com.xabe.visitor;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VisitorTest {

  @Test
  public void shouldVisitorNew() throws Exception {
    final Logger logger = spy(LoggerFactory.getLogger(VisitorTest.class));
    final Car car = new Car();

    car.elements()
        .map(element -> switch (element) {
          case Body body -> "Visiting body";
          case Car car_ -> "Visiting car";
          case Engine engine -> "Visiting engine";
        })
        .forEach(logger::info);

    final InOrder inOrder = inOrder(logger);
    inOrder.verify(logger).info("Visiting body");
    inOrder.verify(logger).info("Visiting engine");
    inOrder.verify(logger).info("Visiting car");
  }

}