package sa.edu.kau.fcit.cpit252.project.student;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByEmail(String email);
    boolean existsByUniversityId(String universityId);
    Optional<Student> findByEmail(String email);
}