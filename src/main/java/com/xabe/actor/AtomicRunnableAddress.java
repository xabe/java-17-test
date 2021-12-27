package com.xabe.actor;

import static com.xabe.actor.Actor.Become;
import static com.xabe.actor.Actor.Stay;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import com.xabe.actor.Actor.Address;
import com.xabe.actor.Actor.Behavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AtomicRunnableAddress implements Address, Runnable {

  private static final Logger LOGGER = LoggerFactory.getLogger(AtomicRunnableAddress.class);

  private final AtomicInteger on = new AtomicInteger(0);

  private final Queue<Object> mb = new ConcurrentLinkedQueue<>();

  private final ExecutorService executorService;

  private Behavior behavior;

  public AtomicRunnableAddress(final ExecutorService executorService, final Function<Address, Behavior> initial) {
    this.executorService = executorService;
    this.behavior = m -> (m instanceof Address self) ? Become(initial.apply(self)) : Stay;
  }

  @Override
  public Address tell(final Object msg) {
    this.mb.offer(msg);
    this.async();
    return this;
  }

  @Override
  public void run() {
    try {
      if (this.on.get() == 1) {
        final var m = this.mb.poll();
        if (Objects.nonNull(m)) {
          this.behavior = this.behavior.apply(m).apply(this.behavior);
        }
      }
    } finally {
      this.on.set(0);
      this.async();
    }
  }

  void async() {
    if (!this.mb.isEmpty() && this.on.compareAndSet(0, 1)) {
      try {
        this.executorService.execute(this);
      } catch (final Throwable t) {
        LOGGER.error("Error async", t);
        this.on.set(0);
        throw t;
      }
    }
  }
}
