package com.xabe;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

public class SealedTest {

  @Test
  public void shouldCreateCircle() throws Exception {
    final int radius = 2;

    final Shape result = new Circle(radius);

    assertThat(result, is(notNullValue()));
  }

  @Test
  public void shouldCreateRentangle() throws Exception {
    final int width = 2;
    final int height = 4;

    final Shape result = new Rectangle(width, height);

    assertThat(result, is(notNullValue()));
  }
}
