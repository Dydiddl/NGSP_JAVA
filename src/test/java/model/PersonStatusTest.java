package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonStatusTest {

    @Test
    void active의_화면표시이름은_활성화이다() {
        PersonStatus status = PersonStatus.ACTIVE;

        String result = status.getDisplayName();

        assertEquals("활성화", result);
    }

    @Test
    void inactive의_화면표시이름은_투입불가이다() {
        PersonStatus status = PersonStatus.INACTIVE;

        String result = status.getDisplayName();

        assertEquals("투입불가", result);
    }

    @Test
    void archives의_화면표시이름은_보관이다() {
        PersonStatus status = PersonStatus.ARCHIVED;

        String result = status.getDisplayName();

        assertEquals("보관", result);
    }
}
