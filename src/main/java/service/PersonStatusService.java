package service;

import model.PersonStatus;
import repository.PersonRepository;

public class PersonStatusService {

    private final PersonRepository personRepository;

    public PersonStatusService(
            PersonRepository personRepository
    ) {
        this.personRepository = personRepository;
    }

    public void changeStatus(
            long personId,
            PersonStatus status
    ) {
        if (personId <= 0) {
            throw new IllegalArgumentException(
                    "사람 ID는 1 이상이어야 합니다."
            );
        }

        if (status == null) {
            throw new IllegalArgumentException(
                    "변경할 상태가 필요합니다."
            );
        }

        boolean updated =
                personRepository.updateStatus(personId, status);

        if (!updated) {
            throw new IllegalArgumentException(
                    "해당 ID의 사람을 찾을 수 없습니다."
            );
        }
    }
}