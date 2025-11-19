package sa.edu.kau.fcit.cpit252.project.auth.authenticator;

import sa.edu.kau.fcit.cpit252.project.auth.dto.SignupRequest;
import sa.edu.kau.fcit.cpit252.project.auth.dto.SignupResponse;

public interface UserRegistrar {
    boolean supports(String email);
    SignupResponse register(SignupRequest request);
}
