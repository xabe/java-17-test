package com.xabe;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

import org.junit.jupiter.api.Test;

public class RandomGeneratorTest {

  @Test
  public void shoudldReturnRandomAlgorithmIsJumpable() throws Exception {
    RandomGeneratorFactory.all()
        .filter(RandomGeneratorFactory::isJumpable)
        .filter(factory -> factory.stateBits() > 128)
        .map(fac -> fac.group() + " : " + fac.name())
        .forEach(System.out::println);
  }

  @Test
  public void shouldReturnAllRandomAlgorithm() throws Exception {
    // All available algorithm in Java 17
    RandomGeneratorFactory
        .all()
        .map(fac -> fac.group() + " : " + fac.name())
        .sorted()
        .forEach(System.out::println);
  }

  @Test
  public void shouldGenerateRandomNumber() throws Exception {
    final RandomGenerator randomGenerator = RandomGeneratorFactory.of("Xoshiro256PlusPlus").create(999);

    // 0-10
    final int result = randomGenerator.nextInt(11);
    assertThat(result, is(both(greaterThanOrEqualTo(0)).and(lessThan(11))));

    // 0-20
    final int result2 = randomGenerator.nextInt(21);
    assertThat(result, is(both(greaterThanOrEqualTo(0)).and(lessThan(21))));
  }
}
