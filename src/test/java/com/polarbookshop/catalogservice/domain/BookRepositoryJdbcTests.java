package com.polarbookshop.catalogservice.domain;

import com.polarbookshop.catalogservice.config.DataConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJdbcTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")
class BookRepositoryJdbcTests {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;

    @Test
    void findBookByIsbnWhenExisting() {
        var book = Book.of("1234567890", "Northern Lights", "Philip Pullman", 12.99);
        jdbcAggregateTemplate.insert(book);

        var actualBook = bookRepository.findByIsbn("1234567890");

        assertThat(actualBook).isPresent();
        assertThat(actualBook.get().isbn()).isEqualTo(book.isbn());
    }

    @Test
    void findBookByIsbnWhenNotExisting() {
        var actualBook = bookRepository.findByIsbn("1234567890");

        assertThat(actualBook).isEmpty();
    }

    @Test
    void findBooksByAuthor() {
        var book1 = Book.of("1234567891", "Northern Lights", "Philip Pullman", 12.99);
        var book2 = Book.of("1234567892", "The Subtle Knife", "Philip Pullman", 14.99);
        var book3 = Book.of("1234567893", "The Hobbit", "J.R.R. Tolkien", 10.99);

        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);

        var actualBooks = bookRepository.findByAuthor("Philip Pullman");

        assertThat(actualBooks)
                .hasSize(2)
                .extracting(Book::isbn)
                .containsExactlyInAnyOrder("1234567891", "1234567892");
    }

    @Test
    void findAllBooks() {
        bookRepository.save(Book.of("1234567891", "Book One", "Author One", 10.99));
        bookRepository.save(Book.of("1234567892", "Book Two", "Author Two", 12.99));

        var books = bookRepository.findAll();

        assertThat(books)
                .extracting(Book::isbn)
                .contains("1234567891", "1234567892");
    }

    @Test
    void existsByIsbnWhenExisting() {
        bookRepository.save(Book.of("1234567891", "Book One", "Author One", 10.99));

        assertThat(bookRepository.existsByIsbn("1234567891")).isTrue();
    }

    @Test
    void existsByIsbnWhenNotExisting() {
        assertThat(bookRepository.existsByIsbn("1234567891")).isFalse();
    }

    @Test
    void deleteBookByIsbn() {
        bookRepository.save(Book.of("1234567891", "Book One", "Author One", 10.99));

        bookRepository.deleteByIsbn("1234567891");

        assertThat(bookRepository.findByIsbn("1234567891")).isEmpty();
    }

    @Test
    void createdDateIsSetWhenSavingBook() {
        var book = Book.of("1234567891", "Book One", "Author One", 10.99);

        var savedBook = bookRepository.save(book);

        assertThat(savedBook.createdDate()).isNotNull();
    }

    @Test
    void optimisticLockingPreventsStaleUpdates() {
        var book = Book.of("1234567894", "Original Title", "Author", 12.99);
        bookRepository.save(book);

        var firstCopy = bookRepository.findByIsbn("1234567894").orElseThrow();
        var secondCopy = bookRepository.findByIsbn("1234567894").orElseThrow();

        var firstUpdate = new Book(
                firstCopy.id(),
                firstCopy.isbn(),
                "Updated Title",
                firstCopy.author(),
                firstCopy.price(),
                firstCopy.publisher(),
                firstCopy.createdDate(),
                firstCopy.lastModifiedDate(),
                firstCopy.version()
        );

        bookRepository.save(firstUpdate);

        var staleUpdate = new Book(
                secondCopy.id(),
                secondCopy.isbn(),
                "Stale Title",
                secondCopy.author(),
                secondCopy.price(),
                secondCopy.publisher(),
                secondCopy.createdDate(),
                secondCopy.lastModifiedDate(),
                secondCopy.version()
        );

        assertThatThrownBy(() -> bookRepository.save(staleUpdate))
                .isInstanceOf(OptimisticLockingFailureException.class);
    }
}