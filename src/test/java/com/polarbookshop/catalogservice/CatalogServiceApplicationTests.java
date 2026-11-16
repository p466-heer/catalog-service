package com.polarbookshop.catalogservice;

import com.polarbookshop.catalogservice.domain.Book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class CatalogServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void whenPostRequestThenBookCreated() throws Exception {
		var expected = Book.of("1231231231", "Title", "Author", 9.90);

		mockMvc.perform(post("/books")
						.with(jwt().authorities(() -> "ROLE_employee"))
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
                                {
                                  "isbn": "1231231231",
                                  "title": "Title",
                                  "author": "Author",
                                  "price": 9.90
                                }
                                """))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.isbn").value(expected.isbn()));
	}

	@Test
	void whenPutRequestForNewBookThenReturn201() throws Exception {
		var book = Book.of("1111111111", "New Book", "Author", 15.00);

		mockMvc.perform(put("/books/" + book.isbn())
						.with(jwt().authorities(() -> "ROLE_employee"))
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
                                {
                                  "isbn": "1111111111",
                                  "title": "New Book",
                                  "author": "Author",
                                  "price": 15.00
                                }
                                """))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.isbn").value(book.isbn()));
	}

	@Test
	void whenPutRequestForExistingBookThenReturn200() throws Exception {
		var book = Book.of("2222222222", "Original Title", "Author", 10.00);

		mockMvc.perform(post("/books")
						.with(jwt().authorities(() -> "ROLE_employee"))
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
                                {
                                  "isbn": "2222222222",
                                  "title": "Original Title",
                                  "author": "Author",
                                  "price": 10.00
                                }
                                """))
				.andExpect(status().isCreated());

		mockMvc.perform(put("/books/" + book.isbn())
						.with(jwt().authorities(() -> "ROLE_employee"))
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
                                {
                                  "isbn": "2222222222",
                                  "title": "Updated Title",
                                  "author": "Author",
                                  "price": 20.00
                                }
                                """))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Updated Title"))
				.andExpect(jsonPath("$.price").value(20.00));
	}

	@Test
	void whenBookCreatedUpdatedAndDeletedThenBookNotFound() throws Exception {
		var book = Book.of("3333333333", "Original Title", "Author", 10.00);

		// POST - create a new book
		mockMvc.perform(post("/books")
						.with(jwt().authorities(() -> "ROLE_employee"))
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
                                {
                                  "isbn": "3333333333",
                                  "title": "Original Title",
                                  "author": "Author",
                                  "price": 10.00
                                }
                                """))
				.andExpect(status().isCreated());

		// GET - check that the book exists
		mockMvc.perform(get("/books/" + book.isbn()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Original Title"));

		// PUT - update the existing book
		mockMvc.perform(put("/books/" + book.isbn())
						.with(jwt().authorities(() -> "ROLE_employee"))
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
                                {
                                  "isbn": "3333333333",
                                  "title": "Updated Title",
                                  "author": "Author",
                                  "price": 20.00
                                }
                                """))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Updated Title"))
				.andExpect(jsonPath("$.price").value(20.00));

		// DELETE - remove the book
		mockMvc.perform(delete("/books/" + book.isbn())
						.with(jwt().authorities(() -> "ROLE_employee")))
				.andExpect(status().isNoContent());

		// GET - the deleted book should return 404
		mockMvc.perform(get("/books/" + book.isbn()))
				.andExpect(status().isNotFound());
	}
}
