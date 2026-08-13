package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class PersonCreateTest {
  private static final ContactNumber CONTACT_NUMBER = new ContactNumber("055-123-4567");

  @Test
  void minimumInformationCreatesRegistrationModel() {
    PersonCreate person = new PersonCreate("건설과 담당자", List.of(CONTACT_NUMBER), null, null);
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
        () -> new PersonCreate(displayName, List.of(CONTACT_NUMBER), null, null));
  }

  @Test
  void nullOrEmptyContactNumbersAreRejected() {
    assertThrows(IllegalArgumentException.class,
        () -> new PersonCreate("김 주무관", null, null, null));
    assertThrows(IllegalArgumentException.class,
        () -> new PersonCreate("김 주무관", List.of(), null, null));
  }

  @Test
  void nullContactNumberIsRejected() {
    assertThrows(IllegalArgumentException.class,
        () -> new PersonCreate("김 주무관", Arrays.asList(CONTACT_NUMBER, null), null, null));
  }

  @Test
  void duplicateContactNumberObjectIsRejected() {
    assertThrows(IllegalArgumentException.class,
        () -> new PersonCreate("김 주무관", List.of(CONTACT_NUMBER, CONTACT_NUMBER), null, null));
  }

  @Test
  void numbersWithSameNormalizedValueAreRejected() {
    ContactNumber formatted = new ContactNumber("010-1234-5678");
    ContactNumber digitsOnly = new ContactNumber("01012345678");
    assertThrows(IllegalArgumentException.class,
        () -> new PersonCreate("김 주무관", List.of(formatted, digitsOnly), null, null));
  }

  @Test
  void multipleDifferentContactNumbersAreAllowed() {
    ContactNumber mobile = new ContactNumber("010-1234-5678");
    PersonCreate person = new PersonCreate(
        "김 주무관", List.of(CONTACT_NUMBER, mobile), null, null);
    assertEquals(List.of(CONTACT_NUMBER, mobile), person.contactNumbers());
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " ", "\t"})
  void blankOptionalInformationIsRejected(String blank) {
    assertThrows(IllegalArgumentException.class,
        () -> new PersonCreate("김 주무관", List.of(CONTACT_NUMBER), blank, null));
    assertThrows(IllegalArgumentException.class,
        () -> new PersonCreate("김 주무관", List.of(CONTACT_NUMBER), null, blank));
  }
}
