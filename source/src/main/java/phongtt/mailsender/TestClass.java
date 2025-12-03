package phongtt.mailsender;

import java.util.List;

/**
 *
 * @author Phong
 */
public class TestClass {
  public static void main(String[] args) {

        GmailSender.init("trantuanphong.ttt@gmail.com", "vzsb lfig erwn zfqh", true);
        
        GmailSender sender = GmailSender.getInstance();

        EmailData email = new EmailData(
                List.of("trantuanphong.ttt@gmail.com"),
                "Email with Attachment",
                "<h1>Hello!</h1><p>This email has an attachment.</p>"
        );

        sender.send(email);
    }   
}
