package cloud.deuterium.wb.books;

import cloud.deuterium.wb.books.entity.Book;
import cloud.deuterium.wb.books.dto.CreateBookRequest;
import cloud.deuterium.wb.books.dto.UpdateBookRequest;
import cloud.deuterium.wb.security.JwtService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Milan Stojkovic 26-Mar-2024
 */

@Slf4j
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService service;
    private final JwtService jwtService;

    public BookController(BookService service, JwtService jwtService) {
        this.service = service;
        this.jwtService = jwtService;
    }

    @GetMapping()
    public Page<Book> getAll(@RequestParam(required = false) String title,
                             @RequestParam(required = false) String description,
                             @PageableDefault Pageable pageable) {
        log.info("Get all Books, with query params: title={} and description={}", title, description);
        return service.getAll(title, description, pageable);
    }

//    @GetMapping(value = "/{uuid}", produces = {"application/xml", "application/json"})
    @GetMapping("/{uuid}")
    public Book getOne(@PathVariable String uuid) {
        log.info("Get Book with id={}", uuid);
        return service.getOne(uuid);
    }

    @PostMapping
    public Book create(@Valid @RequestBody CreateBookRequest book,
                       @RequestHeader("Authorization") String token) {
        String email = jwtService.extractUsername(token.replace("Bearer ", ""));
        log.info("Get Book with title={}", book.getTitle());
        return service.create(book, email);
    }

    @PutMapping("/{uuid}")
    public Book update(@Valid @RequestBody UpdateBookRequest book,
                       @PathVariable String uuid,
                       @RequestHeader("Authorization") String token) {
        String email = jwtService.extractUsername(token.replace("Bearer ", ""));
        log.info("Update Book with title={}", book.getTitle());
        book.setUuid(uuid);
        return service.update(book, email);
    }

    @DeleteMapping("/{uuid}")
    public void unpublish(@PathVariable String uuid, @RequestHeader("Authorization") String token) {
        String email = jwtService.extractUsername(token.replace("Bearer ", ""));
        log.info("Unpublish Book with id={}", uuid);
        service.unpublish(uuid, email);
    }
}
