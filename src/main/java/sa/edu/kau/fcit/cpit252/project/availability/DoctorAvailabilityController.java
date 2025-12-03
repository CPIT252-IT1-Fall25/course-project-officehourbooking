package sa.edu.kau.fcit.cpit252.project.availability;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import sa.edu.kau.fcit.cpit252.project.booking.BookingRepository;
import sa.edu.kau.fcit.cpit252.project.doctor.Doctor;
import sa.edu.kau.fcit.cpit252.project.doctor.DoctorRepository;

@RestController
@RequestMapping("/api/availability")
@CrossOrigin(origins = "*")
public class DoctorAvailabilityController {

    private final DoctorAvailabilityRepository repo;
    private final DoctorRepository doctorRepository;
    private final BookingRepository bookingRepository;

    public DoctorAvailabilityController(
            DoctorAvailabilityRepository repo,
            DoctorRepository doctorRepository,
            BookingRepository bookingRepository) {
        this.repo = repo;
        this.doctorRepository = doctorRepository;
        this.bookingRepository = bookingRepository;
    }


    @GetMapping("/doctor/{doctorId}")
    public List<DoctorAvailability> getDoctorAvailability(@PathVariable Long doctorId) {
        return repo.findByDoctorId(doctorId);
    }


    @GetMapping("/doctor/{doctorId}/day/{dayOfWeek}")
    public List<DoctorAvailability> getDoctorAvailabilityByDay(
            @PathVariable Long doctorId,
            @PathVariable DayOfWeek dayOfWeek) {
        return repo.findByDoctorIdAndDayOfWeek(doctorId, dayOfWeek);
    }
    @GetMapping("/doctor/{doctorId}/available-dates")
    public List<LocalDate> getAvailableDates(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        if (!doctorRepository.existsById(doctorId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found");
        }

        List<LocalDate> datesWithSlots = new ArrayList<>();
        LocalDate current = startDate;

        while (!current.isAfter(endDate)) {
            DayOfWeek dayOfWeek = current.getDayOfWeek();
            List<DoctorAvailability> availabilities = repo.findByDoctorIdAndDayOfWeek(doctorId, dayOfWeek);

            if (!availabilities.isEmpty() && !current.isBefore(LocalDate.now())) {
                // Check for any available slots for this date
                LocalDateTime dayStart = current.atStartOfDay();
                List<LocalDateTime> bookedTimes = bookingRepository.findBookedStartTimesByDoctorAndDate(doctorId, dayStart);

                // Total possible slots
                int totalSlots = 0;
                for (DoctorAvailability availability : availabilities) {
                    LocalTime currentTime = availability.getStartTime();
                    while (currentTime.plusMinutes(availability.getSlotMinutes()).compareTo(availability.getEndTime()) <= 0) {
                        totalSlots++;
                        currentTime = currentTime.plusMinutes(availability.getSlotMinutes());
                    }
                }

                // date has availability
                if (bookedTimes.size() < totalSlots) {
                    datesWithSlots.add(current);
                }
            }

            current = current.plusDays(1);
        }

        return datesWithSlots;
    }
    @GetMapping("/doctor/{doctorId}/slots")
    public List<AvailableSlot> getAvailableSlots(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        if (!doctorRepository.existsById(doctorId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found");
        }

        DayOfWeek dayOfWeek = date.getDayOfWeek();

        List<DoctorAvailability> availabilities = repo.findByDoctorIdAndDayOfWeek(doctorId, dayOfWeek);

        if (availabilities.isEmpty()) {
            return new ArrayList<>();
        }

        // get booked times
        LocalDateTime dayStart = date.atStartOfDay();
        LocalDateTime dayEnd = date.atTime(23, 59, 59);
        List<LocalDateTime> bookedTimes = bookingRepository.findBookedStartTimesByDoctorAndDate(doctorId, dayStart);

        // Generate available slots
        List<AvailableSlot> slots = new ArrayList<>();

        for (DoctorAvailability availability : availabilities) {
            LocalTime currentTime = availability.getStartTime();
            LocalTime endTime = availability.getEndTime();
            int slotMinutes = availability.getSlotMinutes();

            while (currentTime.plusMinutes(slotMinutes).compareTo(endTime) <= 0) {
                LocalDateTime slotStart = LocalDateTime.of(date, currentTime);
                LocalDateTime slotEnd = slotStart.plusMinutes(slotMinutes);

                // Check slot status
                boolean isBooked = bookedTimes.contains(slotStart);

                if (date.isAfter(LocalDate.now()) ||
                        (date.isEqual(LocalDate.now()) && currentTime.isAfter(LocalTime.now()))) {

                    if (!isBooked) {
                        slots.add(new AvailableSlot(slotStart, slotEnd, true));
                    }
                }

                currentTime = currentTime.plusMinutes(slotMinutes);
            }
        }

        return slots;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorAvailability create(@Valid @RequestBody AvailabilityRequest request) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Doctor not found"));

        if (repo.hasOverlappingAvailability(
                request.getDoctorId(),
                request.getDayOfWeek(),
                request.getStartTime(),
                request.getEndTime())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Overlapping availability exists for this time slot");
        }

        DoctorAvailability availability = new DoctorAvailability();
        availability.setDoctor(doctor);
        availability.setDayOfWeek(request.getDayOfWeek());
        availability.setStartTime(request.getStartTime());
        availability.setEndTime(request.getEndTime());
        availability.setSlotMinutes(request.getSlotMinutes());

        return repo.save(availability);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Availability not found");
        }
        repo.deleteById(id);
    }
}