package sa.edu.kau.fcit.cpit252.project.booking;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import sa.edu.kau.fcit.cpit252.project.doctor.Doctor;
import sa.edu.kau.fcit.cpit252.project.doctor.DoctorRepository;
import sa.edu.kau.fcit.cpit252.project.student.Student;
import sa.edu.kau.fcit.cpit252.project.student.StudentRepository;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final DoctorRepository doctorRepository;
    private final StudentRepository studentRepository;

    public BookingService(BookingRepository bookingRepository,
            DoctorRepository doctorRepository,
            StudentRepository studentRepository) {
        this.bookingRepository = bookingRepository;
        this.doctorRepository = doctorRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional
    public Booking createBooking(Long doctorId, Long studentId, LocalDateTime startTime) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));

        if (bookingRepository.existsByDoctorIdAndStartTime(doctorId, startTime)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Time slot already booked");
        }

        Booking booking = new Booking();
        booking.setDoctor(doctor);
        booking.setStudent(student);
        booking.setStartTime(startTime);
        booking.setEndTime(startTime.plusMinutes(15));
        booking.setStatus(BookingStatus.CONFIRMED);

        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking already cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }

    public List<Booking> getDoctorBookings(Long doctorId) {
        return bookingRepository.findByDoctorIdAndStatus(doctorId, BookingStatus.CONFIRMED);
    }

    public List<Booking> getStudentBookings(Long studentId) {
        return bookingRepository.findByStudentIdAndStatus(studentId, BookingStatus.CONFIRMED);
    }
}
