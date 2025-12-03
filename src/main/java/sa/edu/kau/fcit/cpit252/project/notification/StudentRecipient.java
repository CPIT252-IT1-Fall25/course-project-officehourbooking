package sa.edu.kau.fcit.cpit252.project.notification;

import sa.edu.kau.fcit.cpit252.project.student.Student;

public class StudentRecipient implements NotificationRecipient {

    private final Student student;

    public StudentRecipient(Student student) {
        this.student = student;
    }

    @Override
    public void sendNotification(String message) {
        System.out.println("Email to " + student.getEmail() + ": " + message);
    }

    @Override
    public String getRecipientInfo() {
        return "Student: " + student.getName() + " (" + student.getEmail() + ")";
    }

    public Student getStudent() {
        return student;
    }
}
