package sa.edu.kau.fcit.cpit252.project.auth;

import org.springframework.stereotype.Service;
import sa.edu.kau.fcit.cpit252.project.auth.authenticator.UserAuthenticator;
import sa.edu.kau.fcit.cpit252.project.auth.dto.LoginRequest;
import sa.edu.kau.fcit.cpit252.project.auth.dto.LoginResponse;
import sa.edu.kau.fcit.cpit252.project.auth.factory.UserAuthenticatorFactory;

@Service
public class AuthService {
    
    private final UserAuthenticatorFactory authenticatorFactory;
    
    public AuthService(UserAuthenticatorFactory authenticatorFactory) {
        this.authenticatorFactory = authenticatorFactory;
    }
    
    public LoginResponse login(LoginRequest request) {
        String email = request.getEmail().toLowerCase();
        String password = request.getPassword();
        
        UserAuthenticator authenticator = authenticatorFactory.createAuthenticator(email);
        return authenticator.authenticate(email, password);
    }
}