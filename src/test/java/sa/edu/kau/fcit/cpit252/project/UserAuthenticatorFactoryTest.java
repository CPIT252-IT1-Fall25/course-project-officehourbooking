package sa.edu.kau.fcit.cpit252.project;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import sa.edu.kau.fcit.cpit252.project.auth.authenticator.UserAuthenticator;
import sa.edu.kau.fcit.cpit252.project.auth.factory.UserAuthenticatorFactory;

@DisplayName("UserAuthenticatorFactory Tests")
class UserAuthenticatorFactoryTest {

    private UserAuthenticatorFactory factory;
    private UserAuthenticator studentAuthenticator;
    private UserAuthenticator doctorAuthenticator;

    @BeforeEach
    void setUp() {
        studentAuthenticator = mock(UserAuthenticator.class);
        doctorAuthenticator = mock(UserAuthenticator.class);

        when(studentAuthenticator.supports(anyString())).thenAnswer(invocation -> {
            String email = invocation.getArgument(0);
            return email != null && email.toLowerCase().endsWith("@stu.kau.edu.sa");
        });

        when(doctorAuthenticator.supports(anyString())).thenAnswer(invocation -> {
            String email = invocation.getArgument(0);
            return email != null && email.toLowerCase().endsWith("@kau.edu.sa")
                    && !email.toLowerCase().endsWith("@stu.kau.edu.sa");
        });

        List<UserAuthenticator> authenticators = Arrays.asList(
                studentAuthenticator,
                doctorAuthenticator
        );

        factory = new UserAuthenticatorFactory(authenticators);
    }

    @Test
    @DisplayName("Should return student authenticator for student email")
    void testCreateAuthenticator_StudentEmail_ReturnsStudentAuthenticator() {
        String studentEmail = "student@stu.kau.edu.sa";

        UserAuthenticator result = factory.createAuthenticator(studentEmail);

        assertNotNull(result);
        assertEquals(studentAuthenticator, result);
        verify(studentAuthenticator).supports(studentEmail);
    }

    @Test
    @DisplayName("Should return doctor authenticator for faculty email")
    void testCreateAuthenticator_DoctorEmail_ReturnsDoctorAuthenticator() {

        String doctorEmail = "doctor@kau.edu.sa";

        UserAuthenticator result = factory.createAuthenticator(doctorEmail);

        assertNotNull(result);
        assertEquals(doctorAuthenticator, result);
        verify(doctorAuthenticator).supports(doctorEmail);
    }

    @Test
    @DisplayName("Should throw exception for invalid email domain")
    void testCreateAuthenticator_InvalidDomain_ThrowsException() {

        String invalidEmail = "user@gmail.com";

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> factory.createAuthenticator(invalidEmail)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        String reason = exception.getReason();
        assertNotNull(reason, "Exception reason should not be null");
        assertTrue(reason.contains("Invalid email domain"));
        assertTrue(reason.contains("@stu.kau.edu.sa"));
        assertTrue(reason.contains("@kau.edu.sa"));
    }

    @Test
    @DisplayName("Should throw exception for empty email")
    void testCreateAuthenticator_EmptyEmail_ThrowsException() {

        String emptyEmail = "";

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> factory.createAuthenticator(emptyEmail)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    @DisplayName("Should throw exception when no authenticators support the email")
    void testCreateAuthenticator_NoSupportingAuthenticator_ThrowsException() {

        UserAuthenticatorFactory emptyFactory = new UserAuthenticatorFactory(Collections.emptyList());
        String email = "test@stu.kau.edu.sa";

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> emptyFactory.createAuthenticator(email)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    @DisplayName("Should handle case-insensitive email matching")
    void testCreateAuthenticator_MixedCaseEmail_ReturnsCorrectAuthenticator() {

        String mixedCaseEmail = "Student@STU.KAU.EDU.SA";

        UserAuthenticator result = factory.createAuthenticator(mixedCaseEmail);

        assertNotNull(result);
        assertEquals(studentAuthenticator, result);
    }

    @Test
    @DisplayName("Should prioritize first matching authenticator")
    void testCreateAuthenticator_MultipleMatches_ReturnsFirstMatch() {

        UserAuthenticator firstAuth = mock(UserAuthenticator.class);
        UserAuthenticator secondAuth = mock(UserAuthenticator.class);

        when(firstAuth.supports(anyString())).thenReturn(true);
        when(secondAuth.supports(anyString())).thenReturn(true);

        UserAuthenticatorFactory testFactory = new UserAuthenticatorFactory(
                Arrays.asList(firstAuth, secondAuth)
        );

        UserAuthenticator result = testFactory.createAuthenticator("test@example.com");

        assertEquals(firstAuth, result);
        verify(firstAuth).supports("test@example.com");
        verify(secondAuth, never()).supports(anyString());
    }
}
