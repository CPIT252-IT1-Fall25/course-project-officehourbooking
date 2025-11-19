package sa.edu.kau.fcit.cpit252.project.auth.authenticator;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sa.edu.kau.fcit.cpit252.project.auth.dto.SignupRequest;
import sa.edu.kau.fcit.cpit252.project.auth.dto.SignupResponse;
import sa.edu.kau.fcit.cpit252.project.student.Student;
import sa.edu.kau.fcit.cpit252.project.student.StudentRepository;

@Service
public class StudentRegistrar implements UserRegistrar {
    private static final String STUDENT_EMAIL_DOMAIN = "@stu.kau.edu.sa";
    private final StudentRepository studentRepository;

    public StudentRegistrar(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public boolean supports(String email) {
        return email.toLowerCase().trim().endsWith("@stu.kau.edu.sa");
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
        st.setPassword(request.getPassword());
        st.setUniversityId(request.getUniversityId());

        studentRepository.save(st);

        return new SignupResponse(request.getEmail(),request.getName(),"STUDENT","Student signup successful");
    }
}