package sa.edu.kau.fcit.cpit252.project.student;

import jakarta.annotation.Generated;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "students", uniqueConstraints = @UniqueConstraint(name = "uk_student_email", columnNames = "email"))
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank @Email
    private String email;



    // Getters and Setters
    public Long getId() {
        return id;

    }

    public String getName() {
        return name;

    }

    public String getEmail() {
        return email;

    }

    public void setId(Long id) {
        this.id = id;

    }

    public void setName(String name) {
        this.name = name;

    }

    public void setEmail(String email) {
        this.email = email;

    }
}