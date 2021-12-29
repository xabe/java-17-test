package com.xabe.actor;

import static com.xabe.actor.TypedActor.Become;
import static com.xabe.actor.TypedActor.Stay;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import com.xabe.actor.TypedActor.Address;
import com.xabe.actor.TypedActor.Effect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Vending {

  private static final Logger LOGGER = LoggerFactory.getLogger(Vending.class);

  sealed interface Vend {

  }

  record Coin(int amount) implements Vend {

    public Coin {
      if (amount < 1 && amount > 100) {
        throw new AssertionError("1 <= amount < 100");
      }
    }
  }

  record Choice(String product, CompletableFuture<Purchase> purchase) implements Vend {

  }

  record Purchase(String product, int change) {

  }

  public CompletableFuture<Purchase> buy(final String product, final int[] coins) {
    final CompletableFuture<Purchase> completableFuture = new CompletableFuture<>();
    final var actorSystem = new TypedActor.System(Executors.newCachedThreadPool());
    final var vendingMachine = actorSystem.actorOf((Address<Vend> self) -> (Vend msg) -> this.initial(msg));
    Arrays.stream(coins).boxed().forEach(coin -> {
      vendingMachine.tell(new Coin(coin));
    });
    vendingMachine.tell(new Choice(product, completableFuture));
    return completableFuture;
  }

  private Effect<Vend> initial(final Vend message) {
    return switch (message) {
      case Coin c -> {
        LOGGER.info("Received first coin: {}", c.amount);
        yield Become(m -> this.waitCoin(m, c.amount()));
      }
      default -> Stay(); // ignore message, stay in this state
    };
  }

  private Effect<Vend> waitCoin(final Object message, final int counter) {
    return switch (message) {
      case Coin c && counter + c.amount() < 100 -> {
        final var count = counter + c.amount();
        LOGGER.info("Received coin: " + count + " of 100");
        yield Become(m -> this.waitCoin(m, count));
      }
      case Coin c -> {
        final var count = counter + c.amount();
        LOGGER.info("Received last coin: " + count + " of 100");
        final var change = counter + c.amount() - 100;
        yield Become(m -> this.vend(m, change));
      }
      default -> Stay(); // ignore message, stay in this state
    };
  }

  private Effect<Vend> vend(final Object message, final int change) {
    return switch (message) {
      case Choice c -> {
        final String product = c.product();
        this.vendProduct(product);
        c.purchase().complete(new Purchase(product, change));
        this.releaseChange(change);
        yield Become(this::initial);
      }
      default -> Stay(); // ignore message, stay in this state
    };
  }

  void vendProduct(final String product) {
    LOGGER.info("VENDING: " + product);
  }

  void releaseChange(final int change) {
    LOGGER.info("CHANGE: " + change);
  }


}
