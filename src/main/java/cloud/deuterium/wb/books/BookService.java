package cloud.deuterium.wb.books;

import cloud.deuterium.wb.books.entity.Book;
import cloud.deuterium.wb.books.dto.CreateBookRequest;
import cloud.deuterium.wb.books.dto.UpdateBookRequest;
import cloud.deuterium.wb.exceptions.ForbiddenException;
import cloud.deuterium.wb.exceptions.NotFoundException;
import cloud.deuterium.wb.users.UserService;
import cloud.deuterium.wb.users.entity.WookieUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Created by Milan Stojkovic 26-Mar-2024
 */

@Service
public class BookService {

    private final BookRepository repository;
    private final UserService userService;

    public BookService(BookRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public Page<Book> getAll(String title, String description, Pageable pageable) {

        Specification<Book> specifications = Specification
                .where(titleIgnoreCaseSpecification(title))
                .and(descriptionIgnoreCaseSpecification(description));

        return repository.findAll(specifications, pageable);
    }

    private Specification<Book> titleIgnoreCaseSpecification(String title) {
        if (!StringUtils.hasText(title)) {
            return null;
        }
        return ((root, cq, cb) -> cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
    }

    private Specification<Book> descriptionIgnoreCaseSpecification(String description) {
        if (description == null) {
            return null;
        }
        return ((root, cq, cb) -> cb.like(root.get("description"), "%" + description + "%"));
    }

    public Book getOne(String uuid) {
        return repository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Book with id=" + uuid + " is not found"));
    }

    @Transactional
    public Book create(CreateBookRequest bookRequest, String email) {

        Book book = new Book();
        book.setTitle(bookRequest.getTitle());
        book.setCover(bookRequest.getCover());
        book.setDescription(bookRequest.getDescription());
        book.setPrice(bookRequest.getPrice());

        WookieUser user = userService.getUserByEmail(email);

        user.addBook(book);

        return repository.save(book);
    }

    @Transactional
    public Book update(UpdateBookRequest bookRequest, String email) {

        Book book = repository.findByIdAndUserEmail(bookRequest.getUuid(), email)
                .orElseThrow(() -> new ForbiddenException("Forbidden resource"));

        book.setTitle(bookRequest.getTitle());
        book.setCover(bookRequest.getCover());
        book.setDescription(bookRequest.getDescription());
        book.setPrice(bookRequest.getPrice());

        return book;
    }

    @Transactional
    public void unpublish(String uuid, String email) {
        Book book = repository.findByIdAndUserEmail(uuid, email)
                .orElseThrow(() -> new ForbiddenException("Forbidden resource"));

        repository.delete(book);
    }
}
