package sa.edu.kau.fcit.cpit252.project.auth.authenticator;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import sa.edu.kau.fcit.cpit252.project.auth.dto.LoginResponse;
import sa.edu.kau.fcit.cpit252.project.student.Student;
import sa.edu.kau.fcit.cpit252.project.student.StudentRepository;

@Component
public class StudentAuthenticator implements UserAuthenticator {

    private static final String STUDENT_EMAIL_DOMAIN = "@stu.kau.edu.sa";
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentAuthenticator(StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean supports(String email) {
        return email.toLowerCase().endsWith(STUDENT_EMAIL_DOMAIN);
    }

    @Override
    public LoginResponse authenticate(String email, String password) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(password, student.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        LoginResponse response = new LoginResponse(
                student.getId(),
                student.getName(),
                student.getEmail(),
                "STUDENT",
                "Login successful"
        );
        response.setUniversityId(student.getUniversityId());
        return response;
    }
}
