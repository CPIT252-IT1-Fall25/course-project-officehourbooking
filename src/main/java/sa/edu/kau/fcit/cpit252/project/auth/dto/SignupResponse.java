package sa.edu.kau.fcit.cpit252.project.auth.dto;

public class SignupResponse {
    private Long id;
    private  String email;
    private  String name;
    private String role;
    private String universityId;
    private String specialty;
    private  String message;

    public SignupResponse(){}

    public SignupResponse( Long id, String email, String name, String role, String message) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getUniversityId() {
        return universityId;
    }

    public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


}
