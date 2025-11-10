package sa.edu.kau.fcit.cpit252.project.student;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository repo;

    public StudentController(StudentRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Student> list() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Student get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "student not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student create(@Valid @RequestBody Student stu) {
        if (repo.existsByEmail(stu.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "student email already exist");
        }
        return repo.save(stu);
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable Long id, @Valid @RequestBody Student stu) {
        Student existing = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "student not found"));

        if (!existing.getEmail().equals(stu.getEmail()) && repo.existsByEmail(stu.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email already exist");
        }
        existing.setName(stu.getName());
        existing.setEmail(stu.getEmail());
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
