package com.xabe;

import java.time.LocalDate;
import java.util.Objects;

public record Person(String name, String surname, LocalDate date) {

  public static String UNKNOWN_SURNAME = "Unknown";

  public Person {
    Objects.requireNonNull(name);
    Objects.requireNonNull(surname);
  }

  public Person(final String name, final String surname) {
    this(name, surname, null);
  }

  public static Person unnamed(final String surname) {
    return new Person("Unnamed", surname);
  }

}
