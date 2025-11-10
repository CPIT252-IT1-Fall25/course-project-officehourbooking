package sa.edu.kau.fcit.cpit252.project.auth;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sa.edu.kau.fcit.cpit252.project.auth.dto.LoginRequest;
import sa.edu.kau.fcit.cpit252.project.auth.dto.LoginResponse;
import sa.edu.kau.fcit.cpit252.project.doctor.Doctor;
import sa.edu.kau.fcit.cpit252.project.doctor.DoctorRepository;
import sa.edu.kau.fcit.cpit252.project.student.Student;
import sa.edu.kau.fcit.cpit252.project.student.StudentRepository;

@Service
public class AuthService {

    private final StudentRepository studentRepository;
    private final DoctorRepository doctorRepository;

    public AuthService(StudentRepository studentRepository, DoctorRepository doctorRepository) {
        this.studentRepository = studentRepository;
        this.doctorRepository = doctorRepository;
    }

    public LoginResponse login(LoginRequest request) {
        String email = request.getEmail().toLowerCase();
        String password = request.getPassword();

        if (email.endsWith("@stu.kau.edu.sa")) {
            return loginStudent(email, password);
        } else if (email.endsWith("@kau.edu.sa")) {
            return loginDoctor(email, password);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid email domain. Use @stu.kau.edu.sa for students or @kau.edu.sa for faculty"
            );
        }
    }

    private LoginResponse loginStudent(String email, String password) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Invalid email or password"
        ));

        if (!password.equals(student.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Invalid email or password"
            );
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

    private LoginResponse loginDoctor(String email, String password) {
        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Invalid email or password"
        ));

        if (!password.equals(doctor.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Invalid email or password"
            );
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
