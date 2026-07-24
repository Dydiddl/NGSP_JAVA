package service;

import model.Person;
import repository.PersonRepository;

import java.util.List;

public class PersonLookupService {

    private final PersonRepository personRepository;

    public PersonLookupService(
            PersonRepository personRepository
    ) {
        this.personRepository = personRepository;
    }

    public List<Person> findByName(String name) {
        return personRepository.findByName(name);
    }

    public List<Person> findByGenderId(int genderId) {
        return personRepository.findByGenderId(genderId);
    }
}