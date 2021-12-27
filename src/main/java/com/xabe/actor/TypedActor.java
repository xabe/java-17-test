package com.xabe.actor;

import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TypedActor {

  private static final Logger LOGGER = LoggerFactory.getLogger(TypedActor.class);

  /*
   * Behavior of an actor is to receive a message of some type and then return an Effect.
   */
  interface Behavior<T> extends Function<T, Effect<T>> {

  }

  /**
   * Effect would be a way to describe a transition between two states of the actor.
   */
  interface Effect<T> extends Function<Behavior<T>, Behavior<T>> {

  }

  interface Address<T> {

    Address<T> tell(T msg);
  }

  static <T> Effect<T> Become(final Behavior<T> like) {
    return old -> like;
  }

  /*
   * Stay means no behavioral change
   */
  static <T> Effect<T> Stay() {
    return current -> current;
  }

  /*
   * Die will effectively turn off the actor, making it inactive.
   */
  static <T> Effect<T> Die() {
    return Become(msg -> {
      LOGGER.info("Dropping msg [{}] due to severe case of death.", msg);
      return Stay();
    });
  }

  public record System(ExecutorService executorService) {

    public <T> Address<T> actorOf(final Function<Address<T>, Behavior<T>> initial) {
      return new AtomicRunnableAddressType<>(this.executorService, initial);
    }
  }
}
