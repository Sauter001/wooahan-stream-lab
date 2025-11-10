package domain.problem.student;

import lombok.Getter;

@Getter
public enum Subject {
    MATH("Math"), ENGLISH("English"), SCIENCE("Science");

    private final String name;

    Subject(String name) {
        this.name = name;
    }
}
