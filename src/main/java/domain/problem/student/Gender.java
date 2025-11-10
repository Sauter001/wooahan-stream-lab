package domain.problem.student;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("M"), FEMALE("F");

    private final String gender;

    Gender(String gender) {
        this.gender = gender;
    }
}
