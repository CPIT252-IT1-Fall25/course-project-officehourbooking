package sa.edu.kau.fcit.cpit252.project.auth;

import org.springframework.stereotype.Service;
import sa.edu.kau.fcit.cpit252.project.auth.authenticator.UserAuthenticator;
import sa.edu.kau.fcit.cpit252.project.auth.authenticator.UserRegistrar;
import sa.edu.kau.fcit.cpit252.project.auth.dto.LoginRequest;
import sa.edu.kau.fcit.cpit252.project.auth.dto.LoginResponse;
import sa.edu.kau.fcit.cpit252.project.auth.dto.SignupRequest;
import sa.edu.kau.fcit.cpit252.project.auth.dto.SignupResponse;
import sa.edu.kau.fcit.cpit252.project.auth.factory.UserAuthenticatorFactory;
import sa.edu.kau.fcit.cpit252.project.auth.factory.UserRegistrarFactory;

@Service
public class AuthService {

    private final UserAuthenticatorFactory authenticatorFactory;
    private final UserRegistrarFactory registrarFactory;



    public AuthService(UserAuthenticatorFactory authenticatorFactory, UserRegistrarFactory registrarFactory) {
        this.authenticatorFactory = authenticatorFactory;
        this.registrarFactory = registrarFactory;
    }

    public LoginResponse login(LoginRequest request) {
        String email = request.getEmail().toLowerCase().trim();
        String password = request.getPassword();

        UserAuthenticator authenticator = authenticatorFactory.createAuthenticator(email);
        return authenticator.authenticate(email, password);
    }

    public SignupResponse signup(SignupRequest request) {
        String email = request.getEmail().toLowerCase();
        UserRegistrar registrar = registrarFactory.createRegistrar(email);
        return registrar.register(request);
    }
}