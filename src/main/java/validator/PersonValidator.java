package validator;

import config.PersonConfig;
import model.Bank;

public final class PersonValidator {

  private PersonValidator() {}

  public static void validatePerson(
      String name, String phone, int genderId, String address, Bank bank, String accountNumber) {
    validateName(name);
    validatePhone(phone);
    validateAddress(address);
    validateBank(bank);
    validateAccountNumber(accountNumber);
  }

  public static void validateName(String name) {

    CommonValidator.requireText(name, "이름");
    CommonValidator.validateLength(
        name, PersonConfig.NAME_MIN_LENGTH, PersonConfig.NAME_MAX_LENGTH, "이름");
    CommonValidator.validateLettersOnly(name, "이름");
  }

  public static void validatePhone(String phone) {

    CommonValidator.requireText(phone, "휴대폰 번호");
    CommonValidator.validateDigitsOnly(phone, "휴대폰 번호");
    CommonValidator.validateLength(
        phone, PersonConfig.PHONE_LENGTH, PersonConfig.PHONE_LENGTH, "휴대폰 번호");
    if (!phone.startsWith(PersonConfig.PHONE_PREFIX)) {
      throw new IllegalArgumentException("전화번호는" + PersonConfig.PHONE_PREFIX + "으로 시작해야 합니다.");
    }
  }

  public static void validateAddress(String address) {
    CommonValidator.requireText(address, "주소");
    CommonValidator.validateLength(
        address, PersonConfig.ADDRESS_MIN_LENGTH, PersonConfig.ADDRESS_MAX_LENGTH, "주소");
  }

  public static void validateBank(Bank bank) {
    if (bank == null) {
      throw new IllegalArgumentException("은행 정보는 필수입니다.");
    }
  }

  public static void validateAccountNumber(String accountNumber) {
    CommonValidator.requireText(accountNumber, "계좌번호");
    CommonValidator.validateDigitsOnly(accountNumber, "계좌번호");
    CommonValidator.validateLength(
        accountNumber,
        PersonConfig.ACCOUNT_NUMBER_MIN_LENGTH,
        PersonConfig.ACCOUNT_NUMBER_MAX_LENGTH,
        "계좌번호");
  }
}
