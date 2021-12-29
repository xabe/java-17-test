package com.xabe.actor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.xabe.actor.Vending.Purchase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VendingTest {

  @Test
  public void shouldGetCoke() throws Exception {
    //Given
    final Vending vending = new Vending();
    final int[] coins = new int[]{50, 30, 40};

    //When
    final Purchase purchase = vending.buy("Coke", coins).get(1, TimeUnit.SECONDS);

    //Then
    assertThat(purchase, is(notNullValue()));
    assertThat(purchase.product(), is("Coke"));
    assertThat(purchase.change(), is(20));
  }

  @Test
  public void shouldNotGetCoke() throws Exception {
    //Given
    final Vending vending = new Vending();
    final int[] coins = new int[]{50, 30};

    //When
    Assertions.assertThrows(TimeoutException.class, () -> vending.buy("Coke", coins).get(1, TimeUnit.SECONDS));
  }

}
