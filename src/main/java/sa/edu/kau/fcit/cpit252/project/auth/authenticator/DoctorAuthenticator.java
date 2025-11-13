package sa.edu.kau.fcit.cpit252.project.auth.authenticator;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import sa.edu.kau.fcit.cpit252.project.auth.dto.LoginResponse;
import sa.edu.kau.fcit.cpit252.project.doctor.Doctor;
import sa.edu.kau.fcit.cpit252.project.doctor.DoctorRepository;

@Component
public class DoctorAuthenticator implements UserAuthenticator {

    private static final String DOCTOR_EMAIL_DOMAIN = "@kau.edu.sa";
    private final DoctorRepository doctorRepository;

    public DoctorAuthenticator(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
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

        if (!password.equals(doctor.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Invalid email or password");
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
