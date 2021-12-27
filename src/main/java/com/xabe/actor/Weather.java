package com.xabe.actor;

import static com.xabe.actor.Actor.Die;
import static com.xabe.actor.Actor.Stay;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import com.xabe.actor.Actor.Address;
import com.xabe.actor.Actor.Behavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Weather {

  private final Logger logger;

  private final HttpClient client;

  private final Actor.System system;

  public Weather() {
    this.logger = LoggerFactory.getLogger(Weather.class);
    this.client = HttpClient.newHttpClient();
    this.system = new Actor.System(Executors.newCachedThreadPool());
  }

  public CompletableFuture<String> call(final Object city) {
    final CompletableFuture<String> completableFuture = new CompletableFuture<>();
    final Address address = this.system.actorOf(self -> this.httpClient(completableFuture));
    address.tell(city);
    return completableFuture;
  }

  private Behavior httpClient(final CompletableFuture<String> completableFuture) {
    return msg -> {
      if (msg instanceof String city) {
        this.system.actorOf(self -> this.httpHandler(self, city, completableFuture));
        return Stay;
      } else {
        System.err.println("Bad argument " + msg);
        completableFuture.completeExceptionally(new IllegalArgumentException());
        return Stay;
      }
    };
  }

  private Behavior httpHandler(final Address self, final String city, final CompletableFuture<String> completableFuture) {
    final var request = HttpRequest.newBuilder()
        .uri(URI.create("https://wttr.in/" + city + "?format=3"))
        .build();
    try {
      final var resp = this.client.send(request, HttpResponse.BodyHandlers.ofString());
      self.tell(resp);
    } catch (final Exception e) {
      self.tell(e);
    }

    return msg -> switch (msg) {
      case HttpResponse resp -> {
        final String result = resp.body().toString();
        this.logger.info(result);
        completableFuture.complete(result);
        yield Die;
      }
      case Exception e -> {
        this.logger.error("Error get wheather {} ", city, e);
        completableFuture.completeExceptionally(e);
        e.printStackTrace();
        yield Die;
      }
      default -> Die;
    };
  }

}
