package sa.edu.kau.fcit.cpit252.project.auth;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sa.edu.kau.fcit.cpit252.project.auth.dto.LoginRequest;
import sa.edu.kau.fcit.cpit252.project.auth.dto.LoginResponse;
import sa.edu.kau.fcit.cpit252.project.auth.dto.SignupRequest;
import sa.edu.kau.fcit.cpit252.project.auth.dto.SignupResponse;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // For frontend integration
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request) {
        SignupResponse response = authService.signup(request);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/check-email")
    public ResponseEntity<String> checkEmailType(@RequestParam String email) {
        if (email.endsWith("@stu.kau.edu.sa")) {
            return ResponseEntity.ok("STUDENT");
        } else if (email.endsWith("@kau.edu.sa")) {
            return ResponseEntity.ok("DOCTOR");
        } else {
            return ResponseEntity.badRequest().body("INVALID_DOMAIN");
        }
    }
}
