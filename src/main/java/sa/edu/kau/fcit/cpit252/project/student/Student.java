package sa.edu.kau.fcit.cpit252.project.student;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@stu\\.kau\\.edu\\.sa$",
            message = "Email must be a valid KAU student email (@stu.kau.edu.sa)")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "University ID is required")
    @Pattern(regexp = "^[0-9]{7}$", message = "University ID must be 7 digits")
    @Column(name = "university_id", unique = true)
    private String universityId;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    public Student() {
    }

    public Student(String name, String email, String universityId, String password) {
        this.name = name;
        this.email = email.toLowerCase();
        this.universityId = universityId;
        this.password = password;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public String getUniversityId() {
        return universityId;
    }

    public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
