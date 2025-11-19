package sa.edu.kau.fcit.cpit252.project.availability;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/availability")
public class DoctorAvailabilityController {

    private final DoctorAvailabilityRepository repo;

    public DoctorAvailabilityController(DoctorAvailabilityRepository repo) {
        this.repo = repo;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorAvailability create(@Valid @RequestBody DoctorAvailability availability) {
        if (repo.hasOverlappingAvailability(
                availability.getDoctor().getId(),
                availability.getDayOfWeek(),
                availability.getStartTime(),
                availability.getEndTime())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Overlapping availability exists");
        }
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
