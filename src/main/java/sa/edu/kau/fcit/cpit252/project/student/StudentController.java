package sa.edu.kau.fcit.cpit252.project.student;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository repo;
    private final PasswordEncoder passwordEncoder;

    public StudentController(StudentRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public List<Student> list() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Student get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "student not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student create(@Valid @RequestBody Student stu) {
        if (repo.existsByEmail(stu.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "student email already exist");
        }
        stu.setPassword(passwordEncoder.encode(stu.getPassword()));
        return repo.save(stu);
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable Long id, @Valid @RequestBody Student stu) {
        Student existing = repo.findById(id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "student not found"));

        if (!existing.getEmail().equals(stu.getEmail()) && repo.existsByEmail(stu.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email already exist");
        }

        if (!existing.getUniversityId().equals(stu.getUniversityId())
                && repo.existsByUniversityId(stu.getUniversityId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "university ID already exist");
        }

        existing.setName(stu.getName());
        existing.setEmail(stu.getEmail());
        existing.setUniversityId(stu.getUniversityId());
        existing.setPassword(passwordEncoder.encode(stu.getPassword()));
        return repo.save(existing);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "student not found");
        }
        repo.deleteById(id);
    }
}
