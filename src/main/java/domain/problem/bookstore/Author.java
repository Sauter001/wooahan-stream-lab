package domain.problem.bookstore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
public class Author {
    private final int id;
    private final String name;
    private final int birthYear;
    private final String nationality;
}
