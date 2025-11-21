package domain.problem.bookstore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
class Book {
    private final String title;
    private final List<Author> authors; // N:M (공저자)
    private final int publishYear;
    private final double price;
    private final Genre genre;
}
