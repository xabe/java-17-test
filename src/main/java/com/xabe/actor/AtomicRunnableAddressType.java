package com.xabe.actor;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import com.xabe.actor.TypedActor.Address;
import com.xabe.actor.TypedActor.Behavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AtomicRunnableAddressType<T> implements Address<T>, Runnable {

  private static final Logger LOGGER = LoggerFactory.getLogger(AtomicRunnableAddress.class);

  private final AtomicInteger on = new AtomicInteger(0);

  private final Queue<T> mbox = new ConcurrentLinkedQueue<>();

  private final ExecutorService executorService;

  private Behavior<T> behavior;

  public AtomicRunnableAddressType(final ExecutorService executorService, final Function<Address<T>, Behavior<T>> initial) {
    this.executorService = executorService;
    this.behavior = initial.apply(this); //
  }

  @Override
  public Address<T> tell(final T msg) {
    this.mbox.offer(msg);
    this.async();
    return this;
  }  // Enqueue the message onto the mailbox and try to schedule for execution

  // Switch ourselves off, and then see if we should be rescheduled for execution
  @Override
  public void run() {
    try {
      if (this.on.get() == 1) {
        final T m = this.mbox.poll();
        if (m != null) {
          this.behavior = this.behavior.apply(m).apply(this.behavior);
        }
      }
    } finally {
      this.on.set(0);
      this.async();
    }
  }

  // If there's something to process, and we're not already scheduled
  void async() {
    if (!this.mbox.isEmpty() && this.on.compareAndSet(0, 1)) {
      // Schedule to run on the Executor and back out on failure
      try {
        this.executorService.execute(this);
      } catch (final Throwable t) {
        this.on.set(0);
        throw t;
      }
    }
  }
}
