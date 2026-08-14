package service;

import model.PersonCreate;
import repository.PersonRepository;

public class PersonRegistrationService {
  private final PersonRepository personRepository;

  public PersonRegistrationService(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  public long register(PersonCreate person) {
    return personRepository.save(person);
  }
}
