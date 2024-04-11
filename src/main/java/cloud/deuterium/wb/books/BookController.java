package cloud.deuterium.wb.books;

import cloud.deuterium.wb.annotations.UserEmail;
import cloud.deuterium.wb.books.entity.Book;
import cloud.deuterium.wb.books.dto.CreateBookRequest;
import cloud.deuterium.wb.books.dto.UpdateBookRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Milan Stojkovic 26-Mar-2024
 */

@Slf4j
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping("/auth")
    public Authentication getAll(Authentication authentication) {
        log.info("GET /auth");
        return authentication;
    }

    @GetMapping()
    public Page<Book> getAll(@RequestParam(required = false) String title,
                             @RequestParam(required = false) String description,
                             @PageableDefault Pageable pageable) {
        log.info("Get all Books, with query params: title={} and description={}", title, description);
        return service.getAll(title, description, pageable);
    }

    @GetMapping("/{uuid}")
    public Book getOne(@PathVariable String uuid) {
        log.info("Get Book with id={}", uuid);
        return service.getOne(uuid);
    }

    @PostMapping
    public Book create(@Valid @RequestBody CreateBookRequest book,
                       @UserEmail String email) {
        log.info("Get Book with title={}", book.getTitle());
        return service.create(book, email);
    }

    @PutMapping("/{uuid}")
    public Book update(@Valid @RequestBody UpdateBookRequest book,
                       @PathVariable String uuid,
                       @UserEmail String email) {
        log.info("Update Book with title={}", book.getTitle());
        book.setUuid(uuid);
        return service.update(book, email);
    }

    @DeleteMapping("/{uuid}")
    public void unpublish(@PathVariable String uuid, @UserEmail String email) {
        log.info("Unpublish Book with id={}", uuid);
        service.unpublish(uuid, email);
    }
}
