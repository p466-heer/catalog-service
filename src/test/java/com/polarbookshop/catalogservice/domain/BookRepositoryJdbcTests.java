package com.polarbookshop.catalogservice.domain;

import com.polarbookshop.catalogservice.config.DataConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")
class BookRepositoryJdbcTests {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void findBookByIsbnWhenExisting() {
        var book = Book.of("1234567890", "Northern Lights", "Philip Pullman", 12.99);
        bookRepository.save(book);

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
}
