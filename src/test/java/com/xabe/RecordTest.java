package com.xabe;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RecordTest {

  @Test
  public void shouldCreatePerson() throws Exception {
    //Given
    final String name = "Pepe";
    final String surname = "Perez";
    final LocalDate date = LocalDate.of(1999, 1, 15);

    //When

    final Person person = new Person(name, surname, date);

    //Then
    assertThat(person, is(notNullValue()));
    assertThat(person.name(), is(name));
    assertThat(person.surname(), is(surname));
    assertThat(person.date(), is(date));
  }

  @Test
  public void shouldCreatePersonWithoutDate() throws Exception {
    //Given
    final String name = "Pepe";
    final String surname = "Perez";

    //When

    final Person person = new Person(name, surname);

    //Then
    assertThat(person, is(notNullValue()));
    assertThat(person.name(), is(name));
    assertThat(person.surname(), is(surname));
    assertThat(person.date(), is(nullValue()));
  }

  @Test
  public void shouldThrowExceptionWhenNameIsNull() throws Exception {
    //Given
    final String name = null;
    final String surname = "Perez";

    //When

    Assertions.assertThrows(NullPointerException.class, () -> new Person(name, surname));
  }

  @Test
  public void shouldThrowExceptionWhenSurNameIsNull() throws Exception {
    //Given
    final String name = "Pepe";
    final String surname = null;

    //When

    Assertions.assertThrows(NullPointerException.class, () -> new Person(name, surname));
  }

  @Test
  public void shouldCreatePersonStaticMethod() throws Exception {
    //Given
    final String surname = "Perez";

    //When

    final Person person = Person.unnamed(surname);

    //Then
    assertThat(person, is(notNullValue()));
    assertThat(person.name(), is("Unnamed"));
    assertThat(person.surname(), is(surname));
    assertThat(person.date(), is(nullValue()));
  }

  @Test
  public void givenSamePerson() {
    final String name = "Pepe";
    final String surname = "Perez";

    final Person person1 = new Person(name, surname);
    final Person person2 = new Person(name, surname);

    assertThat(person1, is(equalTo(person2)));
  }

  @Test
  public void givenSameHasCode() {
    final String name = "Pepe";
    final String surname = "Perez";

    final Person person1 = new Person(name, surname);
    final Person person2 = new Person(name, surname);

    assertThat(person1.hashCode(), is(person2.hashCode()));
  }

}