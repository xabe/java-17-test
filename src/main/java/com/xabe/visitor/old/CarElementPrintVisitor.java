package com.xabe.visitor.old;

import org.slf4j.Logger;

public class CarElementPrintVisitor implements CarElementVisitor {

  private final Logger logger;

  public CarElementPrintVisitor(final Logger logger) {
    this.logger = logger;
  }

  @Override
  public void visit(final Body body) {
    this.logger.info("Visiting body");
  }

  @Override
  public void visit(final Car car) {
    this.logger.info("Visiting car");
  }

  @Override
  public void visit(final Engine engine) {
    this.logger.info("Visiting engine");
  }

}
