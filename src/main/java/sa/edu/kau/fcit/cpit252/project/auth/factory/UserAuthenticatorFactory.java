package sa.edu.kau.fcit.cpit252.project.auth.factory;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import sa.edu.kau.fcit.cpit252.project.auth.authenticator.UserAuthenticator;

import java.util.List;

@Component
public class UserAuthenticatorFactory {

    private final List<UserAuthenticator> authenticators;

    public UserAuthenticatorFactory(List<UserAuthenticator> authenticators) {
        this.authenticators = authenticators;
    }

    public UserAuthenticator createAuthenticator(String email) {
        return authenticators.stream()
                .filter(auth -> auth.supports(email))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Invalid email domain. Use @stu.kau.edu.sa for students or @kau.edu.sa for faculty"));
    }
}
