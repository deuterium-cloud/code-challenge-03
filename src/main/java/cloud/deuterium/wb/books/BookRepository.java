package cloud.deuterium.wb.books;

import cloud.deuterium.wb.books.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by Milan Stojkovic 26-Mar-2024
 */

@Repository
public interface BookRepository extends JpaRepository<Book, String>, JpaSpecificationExecutor<Book> {
    Optional<Book> findByIdAndUserEmail(String id, String email);
}
