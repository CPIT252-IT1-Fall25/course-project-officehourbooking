package sa.edu.kau.fcit.cpit252.project.doctor;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByEmail(String email);
    Optional<Doctor> findByEmail(String email);
    
    @Query("SELECT d FROM Doctor d WHERE " +
           "(:name IS NULL OR :name = '' OR LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:specialty IS NULL OR :specialty = '' OR LOWER(d.specialty) LIKE LOWER(CONCAT('%', :specialty, '%')))")
    Page<Doctor> searchByNameAndSpecialty(@Param("name") String name, 
                                         @Param("specialty") String specialty, 
                                         Pageable pageable);
}