package sa.edu.kau.fcit.cpit252.project.doctor;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "doctors")
public class Doctor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @Email(message = "Invalid email format")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@kau\\.edu\\.sa$", 
             message = "Email must be a valid KAU faculty email (@kau.edu.sa)")
    @Column(unique = true)
    private String email;
    
    @NotBlank(message = "Specialty is required")
    private String specialty;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    // Constructors
    public Doctor() {}
    
    public Doctor(String name, String email, String specialty, String password) {
        this.name = name;
        this.email = email.toLowerCase();
        this.specialty = specialty;
        this.password = password;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email.toLowerCase(); }
    
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}