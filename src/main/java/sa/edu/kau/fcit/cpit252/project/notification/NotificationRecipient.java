package sa.edu.kau.fcit.cpit252.project.notification;

public interface NotificationRecipient {

    void sendNotification(String message);

    String getRecipientInfo();
}
