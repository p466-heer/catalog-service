package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookAlreadyExistsException;
import com.polarbookshop.catalogservice.domain.BookNotFoundException;
import com.polarbookshop.catalogservice.domain.BookService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.web.OAuth2ResourceServerWebSecurityAutoConfiguration;

@WebMvcTest(
        controllers = BookController.class,
        excludeAutoConfiguration = {
                OAuth2ResourceServerAutoConfiguration.class,
                OAuth2ResourceServerWebSecurityAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
class BookControllerMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Test
    void whenGetBookNotExistingThenShouldReturn404() throws Exception {
        String isbn = "7373731394";

        given(bookService.viewBookDetails(isbn))
                .willThrow(BookNotFoundException.class);

        mockMvc.perform(get("/books/" + isbn))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "employee")
    void whenPostBookAlreadyExistsThenShouldReturn422() throws Exception {
        var book = Book.of("1234567890", "Title", "Author", 9.90);

        given(bookService.addBookToCatalog(book))
                .willThrow(new BookAlreadyExistsException(book.isbn()));

        mockMvc.perform(post("/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "isbn": "1234567890",
                                  "title": "Title",
                                  "author": "Author",
                                  "price": 9.90
                                }
                                """))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithMockUser(roles = "employee")
    void whenPostBookInvalidThenShouldReturn400() throws Exception {
        mockMvc.perform(post("/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "isbn": "invalid",
                                  "title": "",
                                  "author": "Author",
                                  "price": 9.90
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGetBooksByAuthorThenReturnMatchingBooks() throws Exception {
        var book = Book.of("1234567890", "Northern Lights", "Philip Pullman", 12.99);

        given(bookService.viewBooksByAuthor("Philip Pullman"))
                .willReturn(List.of(book));

        mockMvc.perform(get("/books")
                        .param("author", "Philip Pullman"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isbn").value("1234567890"))
                .andExpect(jsonPath("$[0].author").value("Philip Pullman"))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser(roles = "employee")
    void whenOptimisticLockingFailsThenShouldReturn409() throws Exception {
        var book = Book.of("1234567890", "Title", "Author", 9.90);

        given(bookService.editBookDetails("1234567890", book))
                .willThrow(new OptimisticLockingFailureException(
                        "Book was modified by another request"));

        mockMvc.perform(put("/books/1234567890")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "isbn": "1234567890",
                                  "title": "Title",
                                  "author": "Author",
                                  "price": 9.90
                                }
                                """))
                .andExpect(status().isConflict());
    }
}
