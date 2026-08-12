package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class ContactNumberTest {
  @ParameterizedTest
  @ValueSource(strings = {"010-1234-5678", "01012345678", "010 1234 5678"})
  void mobileInputIsNormalizedAndClassified(String input) {
    ContactNumber contactNumber = new ContactNumber(input);
    assertEquals("01012345678", contactNumber.number());
    assertEquals(ContactType.MOBILE, contactNumber.type());
  }

  @ParameterizedTest
  @CsvSource({"02-123-4567,021234567", "02-1234-5678,0212345678", "055-123-4567,0551234567", "055-1234-5678,05512345678", "051-1234-5678,05112345678"})
  void supportedLandlineIsNormalizedAndClassified(String input, String expectedNumber) {
    ContactNumber contactNumber = new ContactNumber(input);
    assertEquals(expectedNumber, contactNumber.number());
    assertEquals(ContactType.LANDLINE, contactNumber.type());
  }

  @Test
  void internetPhoneIsNormalizedAndClassified() {
    ContactNumber contactNumber = new ContactNumber("070-1234-5678");
    assertEquals("07012345678", contactNumber.number());
    assertEquals(ContactType.INTERNET_PHONE, contactNumber.type());
  }

  @Test
  void personalNumberServiceIsNormalizedAndClassified() {
    ContactNumber contactNumber = new ContactNumber("0505-123-4567");
    assertEquals("05051234567", contactNumber.number());
    assertEquals(ContactType.PERSONAL_NUMBER_SERVICE, contactNumber.type());
  }

  @Test
  void nullIsRejected() {
    assertThrows(IllegalArgumentException.class, () -> new ContactNumber(null));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "   "})
  void blankInputIsRejected(String input) {
    assertThrows(IllegalArgumentException.class, () -> new ContactNumber(input));
  }

  @ParameterizedTest
  @ValueSource(strings = {"010-ABCD-5678", "010@1234#5678", "０１０-1234-5678"})
  void unsupportedCharactersAreRejected(String input) {
    assertThrows(IllegalArgumentException.class, () -> new ContactNumber(input));
  }

  @ParameterizedTest
  @ValueSource(strings = {"010-1234-567", "0101234567", "070-123-4567"})
  void invalidLengthForKnownPrefixIsRejected(String input) {
    assertThrows(IllegalArgumentException.class, () -> new ContactNumber(input));
  }

  @ParameterizedTest
  @ValueSource(strings = {"059-123-4567", "12345678"})
  void unsupportedNumberingPlanIsRejected(String input) {
    assertThrows(IllegalArgumentException.class, () -> new ContactNumber(input));
  }

  @ParameterizedTest
  @ValueSource(strings = {"02-12-4567", "055-12-4567", "055-12345-5678"})
  void invalidLandlineLengthIsRejected(String input) {
    assertThrows(IllegalArgumentException.class, () -> new ContactNumber(input));
  }

  @ParameterizedTest
  @ValueSource(strings = {"0505-12-4567", "0505-1234-5678"})
  void invalidPersonalNumberServiceLengthIsRejected(String input) {
    assertThrows(IllegalArgumentException.class, () -> new ContactNumber(input));
  }
}
