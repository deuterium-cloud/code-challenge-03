package cloud.deuterium.wb.annotations;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static java.util.Objects.nonNull;

/**
 * Created by Milan Stojkovic 09-Apr-2024
 */

@Slf4j
@Aspect
@Component
public class UserEmailAspect {

    // All methods with any params which String params are annotated with @UserEmail
    @Pointcut("execution(* *(.., @cloud.deuterium.wb.annotations.UserEmail (String), ..))")
    public void emailPointcut() {
    }

    @Around("emailPointcut()")
    public Object extractEmail(ProceedingJoinPoint joinPoint) throws Throwable {

        if (!(joinPoint.getSignature() instanceof MethodSignature signature)) {
            return joinPoint.proceed();
        }

        Method method = signature.getMethod();

        Object[] arguments = joinPoint.getArgs();

        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < parameters.length; i++) {

            UserEmail annotation = parameters[i].getAnnotation(UserEmail.class);

            if (nonNull(annotation)) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                if (nonNull(authentication) && authentication.getPrincipal() instanceof User user) {

                    String email = user.getUsername();

                    if (nonNull(email)) {
                        arguments[i] = email;
                    }
                }
            }
        }

        return joinPoint.proceed(arguments);
    }


}
