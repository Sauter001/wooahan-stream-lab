package domain.problem.bookstore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
public class BookUser {
    private final String username;
    private final int age;
    private final List<Book> readBooks;
}