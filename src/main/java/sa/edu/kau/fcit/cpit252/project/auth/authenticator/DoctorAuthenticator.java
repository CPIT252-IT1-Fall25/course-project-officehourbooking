package sa.edu.kau.fcit.cpit252.project.auth.authenticator;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import sa.edu.kau.fcit.cpit252.project.auth.dto.LoginResponse;
import sa.edu.kau.fcit.cpit252.project.doctor.Doctor;
import sa.edu.kau.fcit.cpit252.project.doctor.DoctorRepository;

@Component
public class DoctorAuthenticator implements UserAuthenticator {

    private static final String DOCTOR_EMAIL_DOMAIN = "@kau.edu.sa";
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    public DoctorAuthenticator(DoctorRepository doctorRepository, PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean supports(String email) {
        return email.toLowerCase().endsWith(DOCTOR_EMAIL_DOMAIN);
    }

    @Override
    public LoginResponse authenticate(String email, String password) {
        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        if (!passwordEncoder.matches(password, doctor.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Invalid  password");
        }

        LoginResponse response = new LoginResponse(
                doctor.getId(),
                doctor.getName(),
                doctor.getEmail(),
                "DOCTOR",
                "Login successful"
        );
        response.setSpecialty(doctor.getSpecialty());
        return response;
    }
}
