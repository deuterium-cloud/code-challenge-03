package cloud.deuterium.wb.users;

import cloud.deuterium.wb.exceptions.DuplicateException;
import cloud.deuterium.wb.security.dto.SignupRequest;
import cloud.deuterium.wb.users.entity.WookieUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
    this.repository = repository;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public void signup(SignupRequest request) {
    String email = request.email();
    Optional<WookieUser> existingUser = repository.findByEmail(email);

    if (existingUser.isPresent()) {
      throw new DuplicateException(String.format("User with the email address '%s' already exists.", email));
    }
    String hashedPassword = passwordEncoder.encode(request.password());
    WookieUser user = WookieUser.builder()
            .email(email)
            .pseudonym(request.pseudonym())
            .password(hashedPassword)
            .build();
    repository.save(user);
  }

  public WookieUser getUserByEmail(String email){
    return repository.findByEmail(email)
            .orElseThrow();
  }

}
