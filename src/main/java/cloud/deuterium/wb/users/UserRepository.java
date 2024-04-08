package cloud.deuterium.wb.users;

import cloud.deuterium.wb.users.entity.WookieUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<WookieUser, String> {
    Optional<WookieUser> findByEmail(String email);
}
