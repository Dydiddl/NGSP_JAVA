package service;

import normalizer.PersonNormalizer;
import repository.PersonRepository;
import validator.PersonValidator;

public class PersonUpdateService {
    private final PersonRepository personRepository;

    public PersonUpdateService(PersonRepository personRepository) {

        this.personRepository = personRepository;
    }

    public void updateName(int personId, String name) {
        String normalizedName = PersonNormalizer.normalizeName(name);
        PersonValidator.validateName(normalizedName);

        int updatedRows = personRepository.updateName(personId, normalizedName);

        if (updatedRows != 1) {
            throw new IllegalStateException("사람 이름 수정에 실패했습니다.");
        }
    }

    public void updatePhone(
            int personId,
            String phone
    ){
        String normalizedPhone = PersonNormalizer.normalizePhone(phone);

        PersonValidator.validatePhone(normalizedPhone);

        int updateRows = personRepository.updatePhone(personId, normalizedPhone);

        if (updateRows != 1) {
            throw new IllegalStateException("전화번호 정보 수정에 실패했습니다.");
        }
    }

    public void updateAddress(
            int personId,
            String address
    ){
        String normalizedAddress = PersonNormalizer.normalizeAddress(address);
        PersonValidator.validateAddress(normalizedAddress);

        int updateRows = personRepository.updateAddress(personId, normalizedAddress);
        if (updateRows != 1) {
            throw new IllegalStateException("주소 정보 수정에 실패했습니다.");
        }
    }

    public void updateBank(
            int personId,
            String bankName
    ){
        String normalizedBank = PersonNormalizer.normalizeBank(bankName);
        PersonValidator.validateBank(normalizedBank);

        int updateRows = personRepository.updateBank(personId, normalizedBank);

        if (updateRows != 1) {
            throw new IllegalStateException("은행 정보 수정에 실패했습니다.");
        }
    }
    public void updateAccountNumber(
            int personId,
            String accountNumber
    ){
        String normalizedAccountNumber = PersonNormalizer.normalizeAccountNumber(accountNumber);
        PersonValidator.validateAccountNumber(normalizedAccountNumber);
        int updateRows = personRepository.updateAccountNumber(personId, normalizedAccountNumber);
        if (updateRows != 1) {
            throw new IllegalStateException("계좌번호 정보 수정에 실패했습니다.");
        }
    }

    public void updateBankAccount(
            int personId,
            String bank,
            String accountNumber
    ){
        String normalizedBank = PersonNormalizer.normalizeBank(bank);
        String normalizedAccount = PersonNormalizer.normalizeAccountNumber(accountNumber);

        PersonValidator.validateBank(normalizedBank);
        PersonValidator.validateAccountNumber(normalizedAccount);

        int updateRows = personRepository.updateBankAccount(
                personId,
                normalizedBank,
                normalizedAccount
        );
        if (updateRows != 1) {
            throw new IllegalStateException("통장 정보 수정에 실패했습니다.");
        }
    }

    public void updateGenderId(
            int personId,
            int genderId){
        PersonValidator.validateGenderId(genderId);

        int updateRows = personRepository.updateGenderId(personId, genderId);
        if (updateRows != 1) {
            throw new IllegalStateException("성별 정보 수정에 실패했습니다.");
        }
    }

}
