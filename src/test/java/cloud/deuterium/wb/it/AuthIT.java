package cloud.deuterium.wb.it;

import cloud.deuterium.wb.security.dto.ApiErrorResponse;
import cloud.deuterium.wb.security.dto.LoginRequest;
import cloud.deuterium.wb.security.dto.LoginResponse;
import cloud.deuterium.wb.security.dto.SignupRequest;
import cloud.deuterium.wb.users.UserRepository;
import cloud.deuterium.wb.users.entity.WookieUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static cloud.deuterium.wb.TestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

/**
 * Created by Milan Stojkovic 26-Mar-2024
 */
public class AuthIT extends BaseIT {

    @Autowired
    WebTestClient webClient;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void seedDB() {
        userRepository.deleteAll();
        WookieUser user = createUser();
        userRepository.save(user);
    }

    @DisplayName("Should create new User")
    @Test
    void signup_test() {

        Optional<WookieUser> optional = userRepository.findByEmail("lohgarra1@example.com");
        assertThat(optional.isEmpty()).isTrue();

        SignupRequest signupRequest = new SignupRequest("lohgarra1", "lohgarra1@example.com", "12345678");

        webClient.post()
                .uri("/api/auth/signup")
                .body(Mono.just(signupRequest), SignupRequest.class)
                .exchange()
                .expectStatus().isCreated();

        Optional<WookieUser> optional1 = userRepository.findByEmail("lohgarra1@example.com");
        assertThat(optional1.isPresent()).isTrue();
    }

    @DisplayName("Should throw an exception because User with same mail exists")
    @Test
    void signup_test_failed() {

        Optional<WookieUser> optional = userRepository.findByEmail("lohgarra@example.com");
        assertThat(optional.isPresent()).isTrue();

        SignupRequest signupRequest = new SignupRequest("lohgarra", "lohgarra@example.com", "12345678");

        webClient.post()
                .uri("/api/auth/signup")
                .body(Mono.just(signupRequest), SignupRequest.class)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @DisplayName("Should login User")
    @Test
    void login_test() {
        LoginRequest loginRequest = new LoginRequest("lohgarra@example.com", "12345678");

        webClient.post()
                .uri("/api/auth/login")
                .body(Mono.just(loginRequest), LoginRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoginResponse.class)
                .value(response -> {
                    assertThat(response.email()).isEqualTo("lohgarra@example.com");
                    assertThat(response.token()).isNotBlank();
                });
    }

    @DisplayName("Should return 401 Not Authorized for bad credentials")
    @Test
    void login_test_failed() {
        LoginRequest loginRequest = new LoginRequest("lohgarra@example.com", "bad_password");

        webClient.post()
                .uri("/api/auth/login")
                .header(ACCEPT,APPLICATION_JSON_VALUE)
                .body(Mono.just(loginRequest), LoginRequest.class)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody(ApiErrorResponse.class)
                .value(response -> {
                    assertThat(response.errorCode()).isEqualTo(401);
                    assertThat(response.description()).isEqualTo("Invalid username or password");
                });
    }
}
