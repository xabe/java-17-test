package com.xabe;

import java.io.ObjectInputFilter;
import java.util.function.BinaryOperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintFilterFactory implements BinaryOperator<ObjectInputFilter> {

  private final Logger logger = LoggerFactory.getLogger(PrintFilterFactory.class);

  @Override
  public ObjectInputFilter apply(
      final ObjectInputFilter currentFilter, final ObjectInputFilter nextFilter) {

    this.logger.info("Current filter: " + currentFilter);
    this.logger.info("Requested filter: " + nextFilter);

    // Returns a filter that merges the status of a filter and another filter
    return ObjectInputFilter.merge(nextFilter, currentFilter);

    // some logic and return other filters
    // reject all JComponent classes
          /*return filterInfo -> {
              Class<?> clazz = filterInfo.serialClass();
              if (clazz != null) {
                  if(JComponent.class.isAssignableFrom(clazz)){
                      return ObjectInputFilter.Status.REJECTED;
                  }
              }
              return ObjectInputFilter.Status.ALLOWED;
          };*/

  }

}
