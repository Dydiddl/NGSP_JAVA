package service;

import model.PersonCreate;
import normalizer.PersonNormalizer;
import repository.PersonRepository;
import validator.PersonValidator;


public class PersonRegistrationService {
    private final PersonRepository personRepository;

    public PersonRegistrationService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public long register(
            String name,
            String phone,
            int genderId,
            String address,
            String bank,
            String accountNumber
    ) {
        String normalizedName = PersonNormalizer.normalizeName(name);
        String normalizedPhone = PersonNormalizer.normalizePhone(phone);
        String normalizedAddress = PersonNormalizer.normalizeAddress(address);
        String normalizedBank = PersonNormalizer.normalizeBank(bank);
        String normalizedAccountNumber = PersonNormalizer.normalizeAccountNumber(accountNumber);

        PersonValidator.validatePerson(
                normalizedName,
                normalizedPhone,
                genderId,
                normalizedAddress,
                normalizedBank,
                normalizedAccountNumber
        );

        PersonCreate person = new PersonCreate(
                normalizedName,
                normalizedPhone,
                genderId,
                normalizedAddress,
                normalizedBank,
                normalizedAccountNumber
        );
        return personRepository.save(person);
    }

}
