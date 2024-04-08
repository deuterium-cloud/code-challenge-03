package cloud.deuterium.wb.users;

import cloud.deuterium.wb.users.entity.WookieUser;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {

        WookieUser user = repository.findByEmail(email)
                .orElseThrow(() ->
                        new BadCredentialsException("Bad credentials!"));

        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
