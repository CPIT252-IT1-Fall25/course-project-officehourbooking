package sa.edu.kau.fcit.cpit252.project.auth.factory;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import sa.edu.kau.fcit.cpit252.project.auth.authenticator.UserRegistrar;

import java.util.List;

@Component
public class UserRegistrarFactory {

    private final List<UserRegistrar> registrars;

    public UserRegistrarFactory(List<UserRegistrar> registrars) {
        this.registrars = registrars;
    }

    public UserRegistrar createRegistrar(String email) {


        return registrars.stream()
                .filter(r -> r.supports(email))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid email domain. Use @stu.kau.edu.sa for students or @kau.edu.sa for faculty"
                ));
    }
}
