package sa.edu.kau.fcit.cpit252.project.doctor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorRepository repo;
    private final PasswordEncoder passwordEncoder;

    public DoctorController(DoctorRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public List<Doctor> list() {
        return repo.findAll();
    }

    @GetMapping("/search")
    public Page<Doctor> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String specialty,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        return repo.searchByNameAndSpecialty(name, specialty, pageable);
    }

    @GetMapping("/{id}")
    public Doctor get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "doctor not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Doctor create(@Valid @RequestBody Doctor d) {
        if (repo.existsByEmail(d.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email already in use");
        }
        d.setPassword(passwordEncoder.encode(d.getPassword()));
        return repo.save(d);
    }

    @PutMapping("/{id}")
    public Doctor update(@PathVariable Long id, @Valid @RequestBody Doctor in) {
        Doctor d = repo.findById(id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "doctor not found"));

        if (!d.getEmail().equals(in.getEmail()) && repo.existsByEmail(in.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email already in use");
        }

        d.setName(in.getName());
        d.setEmail(in.getEmail());
        d.setSpecialty(in.getSpecialty());
        d.setPassword(passwordEncoder.encode(in.getPassword()));
        return repo.save(d);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "doctor not found");
        }
        repo.deleteById(id);
    }
}
