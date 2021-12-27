package com.xabe.actor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WeatherTest {

  @Test
  public void shouldGetWeatherMadrid() throws Exception {
    final Weather weather = new Weather();

    final String result = weather.call("madrid").get(1, TimeUnit.SECONDS);

    assertThat(result, is(notNullValue()));
  }

  @Test
  public void shouldNotGetWeather() throws Exception {
    final Weather weather = new Weather();

    Assertions.assertThrows(ExecutionException.class, () -> weather.call(11).get(1, TimeUnit.SECONDS));
  }
  

}
