package cloud.deuterium.wb.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import static java.util.Objects.nonNull;

/**
 * Created by Milan Stojkovic 27-Mar-2024
 */
public class DartVaderInterceptor implements HandlerInterceptor {

    private final String VADER_EMAIL = "vader@deathstar.com";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String method = request.getMethod();

        if ("POST".equals(method)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (nonNull(authentication) && VADER_EMAIL.equals(authentication.getName())) {
                response.setStatus(403);
                return false;
            }
        }

        return true;
    }
}
