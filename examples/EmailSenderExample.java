import phongtt.mailsender.GmailSender;
import phongtt.mailsender.EmailData;

import java.io.File;
import java.util.Arrays;

public class EmailSenderExample {
    public static void main(String[] args) {
        // Step 1: Initialize GmailSender with your Gmail credentials
        String username = "your-email@gmail.com";  // Your Gmail address
        String appPassword = "your-app-password";  // Your Gmail app password (not regular Gmail password)

        // Initialize with asynchronous mode (true)
        GmailSender sender = GmailSender.init(username, appPassword, true);

        // Step 2: Create EmailData object with email content
        EmailData email = new EmailData();
        email.setSubject("Test Email");
        email.setContent("This is a test email with an attachment.");
        email.setHtml(false);  // false means plain text, true means HTML

        // Recipients (TO, CC, BCC)
        email.setTo(Arrays.asList("recipient1@example.com", "recipient2@example.com"));
        email.setCc(Arrays.asList("cc@example.com"));
        email.setBcc(Arrays.asList("bcc@example.com"));

        // Optional: Add an attachment (e.g., a PDF or image)
        File attachment = new File("path/to/your/attachment.pdf");
        email.addAttachment(attachment);

        // Step 3: Send the email (asynchronously)
        sender.send(email);

        System.out.println("Email is being sent...");
    }
}
