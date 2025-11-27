package sa.edu.kau.fcit.cpit252.project.auth.authenticator;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import sa.edu.kau.fcit.cpit252.project.auth.dto.SignupRequest;
import sa.edu.kau.fcit.cpit252.project.auth.dto.SignupResponse;
import sa.edu.kau.fcit.cpit252.project.student.Student;
import sa.edu.kau.fcit.cpit252.project.student.StudentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class StudentRegistrar implements UserRegistrar {
    private static final String STUDENT_EMAIL_DOMAIN = "@stu.kau.edu.sa";
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentRegistrar(StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean supports(String email) {
        return email.toLowerCase().trim().endsWith(STUDENT_EMAIL_DOMAIN);
    }


    @Override
    public SignupResponse register(SignupRequest request) {
        if (studentRepository.findByEmail(request.getEmail()).isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A student already exists with this email."
            );
    }
        Student st = new Student();
        st.setName(request.getName());
        st.setEmail(request.getEmail());
        st.setPassword(passwordEncoder.encode(request.getPassword()));
        st.setUniversityId(request.getUniversityId());

        Student savedStudent = studentRepository.save(st);
        SignupResponse response = new SignupResponse(
                savedStudent.getId(),
                savedStudent.getEmail(),
                savedStudent.getName(),
                "STUDENT",
                "Student signup successful"
        );
        response.setUniversityId(st.getUniversityId());

        return response;
    }
}