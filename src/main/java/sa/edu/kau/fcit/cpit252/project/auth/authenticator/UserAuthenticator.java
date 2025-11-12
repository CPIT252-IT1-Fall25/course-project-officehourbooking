package sa.edu.kau.fcit.cpit252.project.auth.authenticator;

import sa.edu.kau.fcit.cpit252.project.auth.dto.LoginResponse;

public interface UserAuthenticator {

    LoginResponse authenticate(String email, String password);

    boolean supports(String email);
}
