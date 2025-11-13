package sa.edu.kau.fcit.cpit252.project.auth.authenticator;

import org.springframework.http.HttpStatus;
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

        public DoctorRegistrar(DoctorRepository doctorRepository) {
            this.doctorRepository = doctorRepository;
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
            Doctor st = new Doctor();
            st.setName(request.getName());
            st.setEmail(request.getEmail());
            st.setPassword(request.getPassword());
            st.setSpecialty(request.getSpecialty());

            doctorRepository.save(st);

            return new SignupResponse(request.getEmail(),request.getName(),"DOCTOR","Instructor signup successful");
        }
    }

