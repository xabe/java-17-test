package com.xabe;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

public class TextBlockTest {

  String json = """
      {
       "name": "painter",
       "qty": 18,
       "size": {"width": 1, "height": 8, "unit": "in"},
       "tags": ["writing", "pen"],
       "rating": 7
      }
      """;

  String languages = """
      Java     \s
      Python   \s
      Ruby     \s
      Javascript
      """;

  String query = """
      SELECT p FROM Product p WHERE p.name LIKE %?1% \
      OR p.shortDescription LIKE %?1% \
      OR p.fullDescription LIKE %?1% \
      OR p.brand.name LIKE %?1% \
      OR p.category.name LIKE %?1%
      """;

  String jsonFormat = """
      {
       "name": "%s",
       "age": %d
      }
      """;

  @Test
  public void shouldEquals() throws Exception {
    assertThat(this.json,
        is("{\n \"name\": \"painter\",\n \"qty\": 18,\n \"size\": {\"width\": 1, \"height\": 8, \"unit\": \"in\"},\n \"tags\": "
            + "[\"writing\", \"pen\"],\n \"rating\": 7\n}\n"));
    assertThat(this.languages,
        is("Java      \nPython    \nRuby      \nJavascript\n"));
    assertThat(this.query,
        is("SELECT p FROM Product p WHERE p.name LIKE %?1% OR p.shortDescription LIKE %?1% OR p.fullDescription LIKE %?1% OR p"
            + ".brand.name LIKE %?1% OR p.category.name LIKE %?1%\n"));
    assertThat(this.jsonFormat.formatted("pepe", 2), is("{\n \"name\": \"pepe\",\n \"age\": 2\n}\n"));
  }

}
