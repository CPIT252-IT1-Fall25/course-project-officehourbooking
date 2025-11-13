package sa.edu.kau.fcit.cpit252.project.availability;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Long> {

    List<DoctorAvailability> findByDoctorId(Long doctorId);

    List<DoctorAvailability> findByDoctorIdAndDayOfWeek(Long doctorId, DayOfWeek dayOfWeek);

    @Query("SELECT da FROM DoctorAvailability da WHERE da.doctor.id = :doctorId "
            + "AND da.dayOfWeek = :dayOfWeek "
            + "AND (da.validFrom IS NULL OR da.validFrom <= :date) "
            + "AND (da.validTo IS NULL OR da.validTo >= :date)")
    List<DoctorAvailability> findValidAvailability(@Param("doctorId") Long doctorId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("date") LocalDate date);

    @Query("SELECT COUNT(da) > 0 FROM DoctorAvailability da WHERE da.doctor.id = :doctorId "
            + "AND da.dayOfWeek = :dayOfWeek "
            + "AND ((da.startTime <= :startTime AND da.endTime > :startTime) "
            + "OR (da.startTime < :endTime AND da.endTime >= :endTime) "
            + "OR (da.startTime >= :startTime AND da.endTime <= :endTime))")
    boolean hasOverlappingAvailability(@Param("doctorId") Long doctorId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);
}
