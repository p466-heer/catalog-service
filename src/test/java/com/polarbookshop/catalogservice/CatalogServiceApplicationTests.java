
package com.polarbookshop.catalogservice;

import com.polarbookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class CatalogServiceApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void whenPostRequestThenBookCreated() {
		var expected = new Book("1231231231", "Title", "Author", 9.90);

		webTestClient
				.post().uri("/books")
				.bodyValue(expected)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Book.class).value(actual -> {
					assertThat(actual).isNotNull();
					assertThat(actual.isbn()).isEqualTo(expected.isbn());
				});
	}

	@Test
	void whenPutRequestForNewBookThenReturn201() {
		var book = new Book("1111111111", "New Book", "Author", 15.00);

		webTestClient
				.put().uri("/books/" + book.isbn())
				.bodyValue(book)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Book.class).value(actual -> {
					assertThat(actual).isNotNull();
					assertThat(actual.isbn()).isEqualTo(book.isbn());
				});
	}

	@Test
	void whenPutRequestForExistingBookThenReturn200() {
		var book = new Book("2222222222", "Original Title", "Author", 10.00);

		webTestClient
				.post().uri("/books")
				.bodyValue(book)
				.exchange()
				.expectStatus().isCreated();

		var updatedBook = new Book("2222222222", "Updated Title", "Author", 20.00);

		webTestClient
				.put().uri("/books/" + book.isbn())
				.bodyValue(updatedBook)
				.exchange()
				.expectStatus().isOk()
				.expectBody(Book.class).value(actual -> {
					assertThat(actual).isNotNull();
					assertThat(actual.title()).isEqualTo("Updated Title");
					assertThat(actual.price()).isEqualTo(20.00);
				});
	}


	@Test
	void whenBookCreatedUpdatedAndDeletedThenBookNotFound() {
		var book = new Book("3333333333", "Original Title", "Author", 10.00);

		//POST - create a new book
		webTestClient
				.post().uri("/books")
				.bodyValue(book)
				.exchange()
				.expectStatus().isCreated();

		//GET - check that the book exists
		webTestClient
				.get().uri("/books/" + book.isbn())
				.exchange()
				.expectStatus().isOk()
				.expectBody(Book.class).value(actual -> {
					assertThat(actual.title()).isEqualTo("Original Title");
				});

		// PUT - update the existing book
		var updatedBook = new Book(
				"3333333333", "Updated Title", "Author", 20.00
		);

		webTestClient
				.put().uri("/books/" + book.isbn())
				.bodyValue(updatedBook)
				.exchange()
				.expectStatus().isOk()
				.expectBody(Book.class).value(actual -> {
					assertThat(actual.title()).isEqualTo("Updated Title");
					assertThat(actual.price()).isEqualTo(20.00);
				});

		//DELETE - remove the book
		webTestClient
				.delete().uri("/books/" + book.isbn())
				.exchange()
				.expectStatus().isNoContent();

		//GET - the deleted book should return 404
		webTestClient
				.get().uri("/books/" + book.isbn())
				.exchange()
				.expectStatus().isNotFound();
	}

}
