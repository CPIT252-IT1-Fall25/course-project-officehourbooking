package sa.edu.kau.fcit.cpit252.project.auth.dto;

public class SignupResponse {

    private  String email;
    private  String name;
    private String role;
    private  String message;

    public SignupResponse(){}

    public SignupResponse( String email, String name, String role, String message) {
        this.email = email;
        this.name = name;
        this.role = role;
        this.message = message;
    }

    public String getEmail() {
        return email;
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
