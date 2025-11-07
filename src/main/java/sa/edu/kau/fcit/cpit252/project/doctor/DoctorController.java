package sa.edu.kau.fcit.cpit252.project.doctor;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    private final DoctorRepository repo;

    public DoctorController(DoctorRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Doctor> list() { return repo.findAll(); }

    @GetMapping("/{id}")
    public Doctor get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> 
            new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.NOT_FOUND, "doctor not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Doctor create(@Valid @RequestBody Doctor d) {
        if (repo.existsByEmail(d.getEmail()))
            throw new org.springframework.web.server.ResponseStatusException(
                HttpStatus.BAD_REQUEST, "email already in use");
        return repo.save(d);
    }

    @PutMapping("/{id}")
    public Doctor update(@PathVariable Long id, @Valid @RequestBody Doctor in) {
        Doctor d = repo.findById(id).orElseThrow(() ->
            new org.springframework.web.server.ResponseStatusException(
                HttpStatus.NOT_FOUND, "doctor not found"));
        if (!d.getEmail().equals(in.getEmail()) && repo.existsByEmail(in.getEmail()))
            throw new org.springframework.web.server.ResponseStatusException(
                HttpStatus.BAD_REQUEST, "email already in use");
        d.setName(in.getName());
        d.setEmail(in.getEmail());
        d.setSpecialty(in.getSpecialty());
        return repo.save(d);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!repo.existsById(id))
            throw new org.springframework.web.server.ResponseStatusException(
                HttpStatus.NOT_FOUND, "doctor not found");
        repo.deleteById(id);
    }
}

