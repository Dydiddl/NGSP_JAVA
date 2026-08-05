package model;

public final class ResidentRegistrationNumber {
  private final String value;

  public ResidentRegistrationNumber(String value) {
    this.value = normalizer(value);
    validate(this.value);
  }
}
