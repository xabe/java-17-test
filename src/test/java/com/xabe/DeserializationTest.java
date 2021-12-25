package com.xabe;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JComponent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DeserializationTest {

  @Test
  public void shouldDeserialization() throws Exception {
    final byte[] bytes = this.convertObjectToStream(new DeserializationExample());
    final InputStream is = new ByteArrayInputStream(bytes);
    final ObjectInputStream ois = new ObjectInputStream(is);

    // Setting a Custom Filter Using a Pattern
    // need full package path
    // the maximum number of bytes in the input stream = 1024
    // allows classes in com.xabe.*
    // allows classes in the java.base module
    // rejects all other classes !*
    final ObjectInputFilter filter1 =
        ObjectInputFilter.Config.createFilter(
            "maxbytes=1024;com.xabe.*;java.base/*;!*");
    ois.setObjectInputFilter(filter1);

    final Object result = ois.readObject();
    assertThat(result, is(notNullValue()));
  }

  @Test
  public void notShouldDeserialization() throws Exception {
    final byte[] bytes = this.convertObjectToStream(new DeserializationExample());
    final InputStream is = new ByteArrayInputStream(bytes);
    final ObjectInputStream ois = new ObjectInputStream(is);

    final ObjectInputFilter filter1 =
        ObjectInputFilter.Config.createFilter(
            "!com.xabe.*;java.base/*;!*");
    ois.setObjectInputFilter(filter1);

    Assertions.assertThrows(InvalidClassException.class, () -> ois.readObject());
  }

  @Test
  public void notShouldDeserializationAllClassesExtendedJComponent() throws Exception {
    final byte[] bytes = this.convertObjectToStream(new JComponentExample());
    final InputStream is = new ByteArrayInputStream(bytes);
    final ObjectInputStream ois = new ObjectInputStream(is);

    final ObjectInputFilter jComponentFilter = ObjectInputFilter.rejectFilter(
        JComponent.class::isAssignableFrom,
        ObjectInputFilter.Status.UNDECIDED);
    ois.setObjectInputFilter(jComponentFilter);

    Assertions.assertThrows(InvalidClassException.class, () -> ois.readObject());
  }

  @Test
  public void notShouldDeserializationWithBinaryOperator() throws Exception {
    final PrintFilterFactory filterFactory = new PrintFilterFactory();
    ObjectInputFilter.Config.setSerialFilterFactory(filterFactory);

    final ObjectInputFilter filter1 = ObjectInputFilter.Config.createFilter("com.xabe.*;java.base/*;!*");
    ObjectInputFilter.Config.setSerialFilter(filter1);

    // Create a filter to allow String.class only
    final ObjectInputFilter intFilter = ObjectInputFilter.allowFilter(
        cl -> cl.equals(String.class), ObjectInputFilter.Status.REJECTED);

    // if pass anything other than String.class, hits filter status: ALLOWED
    //byte[] byteStream =convertObjectToStream("hello");

    // Create input stream
    final byte[] byteStream = this.convertObjectToStream(99);
    final InputStream is = new ByteArrayInputStream(byteStream);
    final ObjectInputStream ois = new ObjectInputStream(is);

    ois.setObjectInputFilter(intFilter);

    Assertions.assertThrows(InvalidClassException.class, () -> ois.readObject());
  }

  private byte[] convertObjectToStream(final Object obj) {
    final ByteArrayOutputStream boas = new ByteArrayOutputStream();
    try (final ObjectOutputStream ois = new ObjectOutputStream(boas)) {
      ois.writeObject(obj);
      return boas.toByteArray();
    } catch (final IOException ioe) {
      ioe.printStackTrace();
    }
    throw new RuntimeException();
  }
}
