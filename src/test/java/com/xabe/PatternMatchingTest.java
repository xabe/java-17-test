package com.xabe;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

public class PatternMatchingTest {

  @Test
  public void patternMatching() throws Exception {
    final String result = this.process("hello");
    assertThat(result, is(notNullValue()));
    assertThat(result, is("yes"));
  }

  private String process(final Object input) {
    return input instanceof String str && str.length() > 4 ? "yes" : "no";
  }

  @Test
  public void recordPatternMatching() throws Exception {
    final Point point = new Point(1, 2);
    final int result = printSum(point);
    assertThat(result, is(3));
  }

  public static int printSum(final Object o) {
    return o instanceof Point p ? p.x() + p.y() : 0;
  }

  @Test
  public void switchPatternMatching() throws Exception {
    final String resultNull = this.switchPatterMatching(null);
    assertThat(resultNull, is(notNullValue()));
    assertThat(resultNull, is("oops"));
    final String resultString = this.switchPatterMatching("string");
    assertThat(resultString, is(notNullValue()));
    assertThat(resultString, is("look: string"));
    final String resultOther = this.switchPatterMatching(1L);
    assertThat(resultOther, is(notNullValue()));
    assertThat(resultOther, is("dunno?"));
  }

  private String switchPatterMatching(final Object object) {
    return switch (object) {
      case null -> "oops";
      case String s -> "look: " + s;
      case default -> "dunno?";
    };
  }

}
