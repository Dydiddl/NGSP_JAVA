package service;

import java.util.List;
import model.Person;
import repository.PersonRepository;

public class PersonSearchService {
  private final PersonRepository personRepository;

  public PersonSearchService(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  public List<Person> findAll() {
    return personRepository.findAll();
  }

  public List<Person> findByName(String name) {
    return personRepository.findByName(name);
  }
}
