package normalizer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TextNormalizerTest {
  @Test
  void keepDigitsOnlyRemovesHyphensAndSpaces() {
    assertEquals("01012345678", TextNormalizer.keepDigitsOnly("010-1234 5678"));
  }

  @Test
  void keepDigitsOnlyRemovesAllNonDigitCharacters() {
    assertEquals("0105678", TextNormalizer.keepDigitsOnly("010-ABCD-5678"));
  }
}
