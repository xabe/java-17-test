package com.xabe.visitor.old;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VisitorOldTest {

  @Test
  public void shouldVisitorOld() throws Exception {
    final Logger logger = spy(LoggerFactory.getLogger(VisitorOldTest.class));
    final Car car = new Car();
    final CarElementPrintVisitor carElementPrintVisitor = new CarElementPrintVisitor(logger);

    car.accept(carElementPrintVisitor);

    final InOrder inOrder = inOrder(logger);
    inOrder.verify(logger).info("Visiting body");
    inOrder.verify(logger).info("Visiting engine");
    inOrder.verify(logger).info("Visiting car");
  }

}
