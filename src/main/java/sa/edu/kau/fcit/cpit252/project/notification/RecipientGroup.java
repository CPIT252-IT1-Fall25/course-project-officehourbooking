package sa.edu.kau.fcit.cpit252.project.notification;

import java.util.ArrayList;
import java.util.List;

public class RecipientGroup implements NotificationRecipient {

    private final String groupName;
    private final List<NotificationRecipient> recipients;

    public RecipientGroup(String groupName) {
        this.groupName = groupName;
        this.recipients = new ArrayList<>();
    }

    public void addRecipient(NotificationRecipient recipient) {
        recipients.add(recipient);
    }

    public void removeRecipient(NotificationRecipient recipient) {
        recipients.remove(recipient);
    }

    @Override
    public void sendNotification(String message) {
        System.out.println("Sending to group '" + groupName + "':");
        for (NotificationRecipient recipient : recipients) {
            recipient.sendNotification(message);
        }
    }

    @Override
    public String getRecipientInfo() {
        StringBuilder info = new StringBuilder("Group: " + groupName + " (" + recipients.size() + " recipients)\n");
        for (NotificationRecipient recipient : recipients) {
            info.append("  - ").append(recipient.getRecipientInfo()).append("\n");
        }
        return info.toString();
    }

    public String getGroupName() {
        return groupName;
    }

    public int getRecipientCount() {
        return recipients.size();
    }
}
