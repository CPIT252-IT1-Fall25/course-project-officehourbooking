package sa.edu.kau.fcit.cpit252.project.notification;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import sa.edu.kau.fcit.cpit252.project.booking.Booking;
import sa.edu.kau.fcit.cpit252.project.doctor.Doctor;
import sa.edu.kau.fcit.cpit252.project.student.Student;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void notifyBookingConfirmation(Booking booking) {
        logger.info("Sending booking confirmation for booking ID: {}", booking.getId());
        RecipientGroup group = new RecipientGroup("Booking Confirmation");

        group.addRecipient(new StudentRecipient(booking.getStudent()));
        group.addRecipient(new DoctorRecipient(booking.getDoctor()));

        String message = String.format("Booking confirmed for %s with Dr. %s",
                booking.getStartTime().format(FORMATTER),
                booking.getDoctor().getName());

        group.sendNotification(message);
    }

    public void notifyBookingCancellation(Booking booking) {
        logger.info("Sending booking cancellation for booking ID: {}", booking.getId());
        RecipientGroup group = new RecipientGroup("Booking Cancellation");

        group.addRecipient(new StudentRecipient(booking.getStudent()));
        group.addRecipient(new DoctorRecipient(booking.getDoctor()));

        String message = String.format("Booking cancelled for %s with Dr. %s",
                booking.getStartTime().format(FORMATTER),
                booking.getDoctor().getName());

        group.sendNotification(message);
    }

    public void notifyMultipleStudents(List<Student> students, String message) {
        logger.info("Sending notification to {} students", students.size());
        RecipientGroup group = new RecipientGroup("Multiple Students");

        for (Student student : students) {
            group.addRecipient(new StudentRecipient(student));
        }

        group.sendNotification(message);
    }

    public void notifyMultipleDoctors(List<Doctor> doctors, String message) {
        logger.info("Sending notification to {} doctors", doctors.size());
        RecipientGroup group = new RecipientGroup("Multiple Doctors");

        for (Doctor doctor : doctors) {
            group.addRecipient(new DoctorRecipient(doctor));
        }

        group.sendNotification(message);
    }

    public void notifyDepartment(List<Doctor> doctors, List<Student> students, String message) {
        logger.info("Sending department notification to {} doctors and {} students",
                doctors.size(), students.size());
        RecipientGroup department = new RecipientGroup("Department Notification");

        RecipientGroup doctorGroup = new RecipientGroup("Faculty");
        for (Doctor doctor : doctors) {
            doctorGroup.addRecipient(new DoctorRecipient(doctor));
        }

        RecipientGroup studentGroup = new RecipientGroup("Students");
        for (Student student : students) {
            studentGroup.addRecipient(new StudentRecipient(student));
        }

        department.addRecipient(doctorGroup);
        department.addRecipient(studentGroup);

        department.sendNotification(message);
    }
}
