package sa.edu.kau.fcit.cpit252.project.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByDoctorIdAndStatus(Long doctorId, BookingStatus status);

    List<Booking> findByStudentIdAndStatus(Long studentId, BookingStatus status);

    List<Booking> findByDoctorIdOrderByStartTimeDesc(Long doctorId);

    List<Booking> findByStudentIdOrderByStartTimeDesc(Long studentId);

    @Query("SELECT b FROM Booking b WHERE b.doctor.id = :doctorId "
            + "AND b.startTime >= :from AND b.startTime <= :to "
            + "ORDER BY b.startTime")
    List<Booking> findDoctorBookingsBetween(@Param("doctorId") Long doctorId,
                                            @Param("from") LocalDateTime from,
                                            @Param("to") LocalDateTime to);

    @Query("SELECT b FROM Booking b WHERE b.student.id = :studentId "
            + "AND b.startTime >= :from AND b.startTime <= :to "
            + "ORDER BY b.startTime")
    List<Booking> findStudentBookingsBetween(@Param("studentId") Long studentId,
                                             @Param("from") LocalDateTime from,
                                             @Param("to") LocalDateTime to);

    @Query("SELECT b.startTime FROM Booking b WHERE b.doctor.id = :doctorId "
            + "AND b.status = 'CONFIRMED' "
            + "AND DATE(b.startTime) = DATE(:date)")
    List<LocalDateTime> findBookedStartTimesByDoctorAndDate(@Param("doctorId") Long doctorId,
                                                            @Param("date") LocalDateTime date);

    boolean existsByDoctorIdAndStartTime(Long doctorId, LocalDateTime startTime);

    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.student.id = :studentId "
            + "AND b.doctor.id = :doctorId "
            + "AND DATE(b.startTime) = DATE(:date) "
            + "AND b.status = 'CONFIRMED'")
    boolean hasActiveBookingForStudentAndDoctorOnDate(@Param("studentId") Long studentId,
                                                      @Param("doctorId") Long doctorId,
                                                      @Param("date") LocalDateTime date);
}