package com.xabe.actor;

import static com.xabe.actor.TypedActor.Become;
import static com.xabe.actor.TypedActor.Die;
import static com.xabe.actor.TypedActor.Stay;

import java.util.concurrent.Executors;

import com.xabe.actor.TypedActor.Address;
import com.xabe.actor.TypedActor.Effect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingPong {

  private static final Logger LOGGER = LoggerFactory.getLogger(PingPong.class);

  private final int max;

  public PingPong(final int max) {
    this.max = max;
  }

  static record Ping(Address<TPong> sender) {

  }

  sealed interface TPong {

  }

  static record Pong(Address<Ping> sender) implements TPong {

  }

  static record DeadlyPong(Address<Ping> sender) implements TPong {

  }

  public void call() {
    final var actorSystem = new TypedActor.System(Executors.newCachedThreadPool());
    final var ponger = actorSystem.actorOf((Address<Ping> self) -> (Ping msg) -> this.pongerBehavior(self, msg, 0));
    final var pinger = actorSystem.actorOf((Address<TPong> self) -> (TPong msg) -> this.pingerBehavior(self, msg));
    ponger.tell(new Ping(pinger));
  }

  private Effect<Ping> pongerBehavior(final Address<Ping> self, final Ping msg, final int counter) {
    return switch (msg) {
      case Ping p && counter < this.max -> {
        LOGGER.info("ping! ➡️");
        p.sender().tell(new Pong(self));
        yield Become(m -> this.pongerBehavior(self, m, counter + 1));
      }
      case Ping p -> {
        LOGGER.info("ping! ☠️");
        p.sender().tell(new DeadlyPong(self));
        yield Die();
      }
    };
  }

  private Effect<TPong> pingerBehavior(final Address<TPong> self, final TPong msg) {
    return switch (msg) {
      case Pong p -> {
        LOGGER.info("pong! ⬅️");
        p.sender().tell(new Ping(self));
        yield Stay();
      }
      case DeadlyPong p -> {
        LOGGER.info("pong! 😵");
        p.sender().tell(new Ping(self));
        yield Die();
      }
    };
  }


}
