package sa.edu.kau.fcit.cpit252.project.booking;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sa.edu.kau.fcit.cpit252.project.notification.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final NotificationService notificationService;

    public BookingController(BookingService bookingService, NotificationService notificationService) {
        this.bookingService = bookingService;
        this.notificationService = notificationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Booking create(@RequestBody BookingRequest request) {
        Booking booking = bookingService.createBooking(
                request.getDoctorId(),
                request.getStudentId(),
                request.getStartTime()
        );
        
        notificationService.notifyBookingConfirmation(booking);
        
        return booking;
    }

    @PutMapping("/{id}/cancel")
    public Booking cancel(@PathVariable Long id) {
        Booking booking = bookingService.cancelBooking(id);
        
        notificationService.notifyBookingCancellation(booking);
        
        return booking;
    }

    @GetMapping("/doctor/{doctorId}")
    public List<Booking> getDoctorBookings(@PathVariable Long doctorId) {
        List<Booking> bookings = bookingService.getDoctorBookings(doctorId);

        return bookings;
    }

    @GetMapping("/student/{studentId}")
    public List<Booking> getStudentBookings(@PathVariable Long studentId) {
        List<Booking> bookings = bookingService.getDoctorBookings(studentId);


        return bookings;
    }

    @GetMapping
    public List<Booking> getBookings(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) BookingStatus status) {

        if (studentId != null && status != null) {
            return bookingService.getStudentBookings(studentId);
        }

        if (doctorId != null && status != null) {
            return bookingService.getDoctorBookings(doctorId);
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Must provide studentId or doctorId with status");
    }
}