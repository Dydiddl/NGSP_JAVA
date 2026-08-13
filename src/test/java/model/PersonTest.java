package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class PersonTest {
  private static final ContactNumber CONTACT_NUMBER = new ContactNumber("010-1234-5678");

  @Test
  void minimumInformationCreatesPerson() {
    Person person = new Person(1L, "건설과 담당자", List.of(CONTACT_NUMBER), null, null,
        PersonStatus.ACTIVE);

    assertEquals("건설과 담당자", person.displayName());
    assertEquals(List.of(CONTACT_NUMBER), person.contactNumbers());
    assertNull(person.name());
    assertNull(person.email());
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" ", "\t"})
  void nullOrBlankDisplayNameIsRejected(String displayName) {
    assertThrows(IllegalArgumentException.class,
        () -> new Person(1L, displayName, List.of(CONTACT_NUMBER), null, null,
            PersonStatus.ACTIVE));
  }

  @Test
  void personWithoutContactNumberIsRejected() {
    assertThrows(IllegalArgumentException.class,
        () -> new Person(1L, "김 주무관", List.of(), null, null, PersonStatus.ACTIVE));
  }

  @Test
  void contactNumbersAreDefensivelyCopied() {
    List<ContactNumber> contacts = new ArrayList<>();
    contacts.add(CONTACT_NUMBER);
    Person person = new Person(1L, "김 주무관", contacts, null, null, PersonStatus.ACTIVE);
    contacts.clear();

    assertEquals(List.of(CONTACT_NUMBER), person.contactNumbers());
    assertThrows(UnsupportedOperationException.class, () -> person.contactNumbers().clear());
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " ", "\t"})
  void blankOptionalInformationIsRejected(String blank) {
    assertThrows(IllegalArgumentException.class,
        () -> new Person(1L, "김 주무관", List.of(CONTACT_NUMBER), blank, null,
            PersonStatus.ACTIVE));
    assertThrows(IllegalArgumentException.class,
        () -> new Person(1L, "김 주무관", List.of(CONTACT_NUMBER), null, blank,
            PersonStatus.ACTIVE));
  }
}
