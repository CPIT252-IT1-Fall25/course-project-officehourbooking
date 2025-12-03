package sa.edu.kau.fcit.cpit252.project.auth.authenticator;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sa.edu.kau.fcit.cpit252.project.auth.dto.SignupRequest;
import sa.edu.kau.fcit.cpit252.project.auth.dto.SignupResponse;
import sa.edu.kau.fcit.cpit252.project.doctor.Doctor;
import sa.edu.kau.fcit.cpit252.project.doctor.DoctorRepository;
@Service
public class DoctorRegistrar implements UserRegistrar {

        private static final String DOCTOR_EMAIL_DOMAIN = "@kau.edu.sa";
        private final DoctorRepository doctorRepository;
        private final PasswordEncoder passwordEncoder;

    public DoctorRegistrar(DoctorRepository doctorRepository, PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
        public boolean supports(String email) {
            return  email.toLowerCase().trim().endsWith(DOCTOR_EMAIL_DOMAIN);
        }

        @Override
        public SignupResponse register(SignupRequest request) {
            if (doctorRepository.findByEmail(request.getEmail()).isPresent()){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "A instructor already exists with this email."
                );
            }
            Doctor dr = new Doctor();
            dr.setName(request.getName());
            dr.setEmail(request.getEmail());
            dr.setPassword(passwordEncoder.encode(request.getPassword()));
            dr.setSpecialty(request.getSpecialty());

            Doctor savedDoctor = doctorRepository.save(dr);

            SignupResponse response = new SignupResponse(
                    savedDoctor.getId(),
                    savedDoctor.getEmail(),
                    savedDoctor.getName(),
                    "DOCTOR",
                    "Instructor signup successful"
            );
            response.setSpecialty(savedDoctor.getSpecialty());

            return response;

        }
    }

