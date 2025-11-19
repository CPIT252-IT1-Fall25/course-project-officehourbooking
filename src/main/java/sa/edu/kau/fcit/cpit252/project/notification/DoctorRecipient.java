package sa.edu.kau.fcit.cpit252.project.notification;

import sa.edu.kau.fcit.cpit252.project.doctor.Doctor;

public class DoctorRecipient implements NotificationRecipient {
    
    private final Doctor doctor;

    public DoctorRecipient(Doctor doctor) {
        this.doctor = doctor;
    }

    @Override
    public void sendNotification(String message) {
        System.out.println("Email to " + doctor.getEmail() + ": " + message);
    }

    @Override
    public String getRecipientInfo() {
        return "Doctor: " + doctor.getName() + " (" + doctor.getSpecialty() + ")";
    }

    public Doctor getDoctor() {
        return doctor;
    }
}