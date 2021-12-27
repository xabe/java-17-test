package com.xabe.actor;

import org.junit.jupiter.api.Test;

public class PingPongTest {

  @Test
  public void pingPong() throws Exception {
    final PingPong pingPong = new PingPong();

    pingPong.call();
  }

}
