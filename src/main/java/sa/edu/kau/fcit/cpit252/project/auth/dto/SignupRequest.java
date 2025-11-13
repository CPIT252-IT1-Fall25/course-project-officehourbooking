package sa.edu.kau.fcit.cpit252.project.auth.dto;

public class SignupRequest {
    private String name;
    private String email;
    private String password;
    private String universityId; // for students
    private String specialty;  // for instructors (doctors)

    public SignupRequest(){

    }
    public SignupRequest(String name, String email, String password, String universityId, String specialty) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.universityId = universityId;
        this.specialty = specialty;
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
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
