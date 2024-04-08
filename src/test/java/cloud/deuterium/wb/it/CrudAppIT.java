package cloud.deuterium.wb.it;

import cloud.deuterium.wb.TestPageImpl;
import cloud.deuterium.wb.books.BookRepository;
import cloud.deuterium.wb.books.entity.Book;
import cloud.deuterium.wb.security.JwtService;
import cloud.deuterium.wb.users.UserRepository;
import cloud.deuterium.wb.users.entity.WookieUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static cloud.deuterium.wb.TestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Milan Stojkovic 26-Mar-2024
 */
public class CrudAppIT extends BaseIT {

    @Autowired
    WebTestClient webClient;

    @Autowired
    JwtService jwtService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookRepository bookRepository;


    @BeforeEach
    void seedDB() {
        bookRepository.deleteAll();
        userRepository.deleteAll();
        WookieUser user = createUser();

        Book book1 = createBookOne();
        Book book2 = createBookTwo();

        user.addBook(book1);
        user.addBook(book2);

        userRepository.save(user);
    }

    @DisplayName("Should return all books")
    @Test
    void get_all_books() {

        webClient.get()
                .uri("/api/books")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(TestPageImpl.class)
                .value(response -> assertThat(response.getFirst().getContent().size()).isEqualTo(2));
    }

    @DisplayName("Should return 401 Not Authorized for POST")
    @Test
    void not_unauthorized_query_post() {
        webClient.post()
                .uri("/api/books")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @DisplayName("Should return 401 Not Authorized for PUT")
    @Test
    void not_unauthorized_query_put() {
        webClient.put()
                .uri("/api/books/c2c0dff9-3b44-417d-bd1f-1120bbad0182")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @DisplayName("Should return 401 Not Authorized for DELETE")
    @Test
    void not_unauthorized_query_delete() {
        webClient.put()
                .uri("/api/books/c2c0dff9-3b44-417d-bd1f-1120bbad0182")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @DisplayName("Should save book")
    @Test
    void save_book() {

        Book book = createBookThree();

        String token = jwtService.generateToken("lohgarra@example.com");

        webClient.post()
                .uri("/api/books")
                .header("Authorization", "Bearer " + token)
                .body(Mono.just(book), Book.class)
                .exchange()
                .expectStatus().isOk();

        List<Book> books = bookRepository.findAll();

        Optional<Book> first = books.stream()
                .filter(b -> "Title Three".equals(b.getTitle()))
                .findFirst();

        assertThat(first.isPresent()).isTrue();
    }

    @DisplayName("Should update book")
    @Test
    void update_book() {

        Book book = bookRepository.findAll()
                .stream()
                .filter(b -> "Title One".equals(b.getTitle()))
                .findFirst()
                .orElseThrow();

        String id = book.getId();

        book.setTitle("Title One Updated");

        String token = jwtService.generateToken("lohgarra@example.com");

        webClient.put()
                .uri("/api/books/" + id)
                .header("Authorization", "Bearer " + token)
                .body(Mono.just(book), Book.class)
                .exchange()
                .expectStatus().isOk();

        Book byId = bookRepository.findById(id)
                .orElseThrow();

        assertThat(byId.getTitle()).isEqualTo("Title One Updated");
    }

    @DisplayName("Should delete book")
    @Test
    void delete_book() {

        Book book = bookRepository.findAll()
                .stream()
                .filter(b -> "Title One".equals(b.getTitle()))
                .findFirst()
                .orElseThrow();

        String id = book.getId();

        String token = jwtService.generateToken("lohgarra@example.com");

        webClient.delete()
                .uri("/api/books/" + id)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .isEmpty();

        Optional<Book> optional = bookRepository.findById(id);

        assertThat(optional.isEmpty()).isTrue();
    }

}
