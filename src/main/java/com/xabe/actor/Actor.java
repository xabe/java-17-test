package com.xabe.actor;

import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Actor {

  private static final Logger LOGGER = LoggerFactory.getLogger(Actor.class);

  /*
   * Behavior of an actor is to receive a message of some type and then return an Effect.
   */
  interface Behavior extends Function<Object, Effect> {

  }

  /**
   * Effect would be a way to describe a transition between two states of the actor.
   */
  interface Effect extends Function<Behavior, Behavior> {

  }

  interface Address {

    Address tell(Object msg);
  }

  static Effect Become(final Behavior like) {
    return old -> like;
  }

  /*
   * Stay means no behavioral change
   */
  static Effect Stay = old -> old;

  /*
   * Die will effectively turn off the actor, making it inactive.
   */
  static Effect Die = Become(msg -> {
    LOGGER.info("Dropping msg [{}] due to severe case of death.", msg);
    return Stay;
  });

  public record System(ExecutorService executorService) {

    public Address actorOf(final Function<Address, Behavior> initial) {
      final var addr = new AtomicRunnableAddress(this.executorService, initial);
      return addr.tell(addr); // Make the actor self aware by seeding its address to the initial behavior
    }
  }
}
