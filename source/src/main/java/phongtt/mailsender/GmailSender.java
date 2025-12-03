package phongtt.mailsender;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import java.io.File;

import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
* @author PhongTT
*/
public class GmailSender {

    private static GmailSender instance; // Biến singleton

    private final String username;       // Email người gửi
    private final Session session;       // Session của Jakarta Mail
    private final boolean async;         // Gửi bất đồng bộ hay đồng bộ

    private final BlockingQueue<EmailData> emailQueue;  // Hàng đợi email
    private final ExecutorService worker;               // Worker thread xử lý queue

    /**
     * Constructor riêng tư (chỉ gọi từ init)
     */
    private GmailSender(String username, String appPassword, boolean async) {
        this.username = username;
        this.async = async;

        // Cấu hình SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Tạo Session
        this.session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, appPassword);
            }
        });

        // Khởi tạo queue và worker thread
        this.emailQueue = new LinkedBlockingQueue<>();
        this.worker = Executors.newSingleThreadExecutor();

        // Bắt đầu worker xử lý email trong queue
        worker.submit(this::processQueue);
    }

    // ------------------ Khởi tạo singleton ------------------

    /**
     * Khởi tạo GmailSender với async mặc định = true
     */
    public static synchronized GmailSender init(String username, String appPassword) {
        return init(username, appPassword, true);
    }

    /**
     * Khởi tạo GmailSender với tùy chọn async
     */
    public static synchronized GmailSender init(String username, String appPassword, boolean async) {
        if (instance != null)
            throw new IllegalStateException("GmailSender đã được khởi tạo");
        instance = new GmailSender(username, appPassword, async);
        return instance;
    }

    /**
     * Lấy instance singleton (phải gọi init trước)
     */
    public static GmailSender getInstance() {
        if (instance == null)
            throw new IllegalStateException("GmailSender chưa được khởi tạo. Gọi init() trước.");
        return instance;
    }

    // ------------------ Gửi email ------------------

    /**
     * Gửi email
     * - Nếu async = true: đưa vào queue để worker gửi
     * - Nếu async = false: gửi trực tiếp
     */
    public void send(EmailData emailData) {
        if (async) {
            emailQueue.offer(emailData); // Thêm vào queue
        } else {
            sendEmail(emailData);        // Gửi đồng bộ
        }
    }

    // ------------------ Xử lý queue ------------------

    /**
     * Worker thread: lấy email từ queue và gửi
     */
    private void processQueue() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                EmailData email = emailQueue.take(); // Block nếu queue rỗng
                sendEmail(email);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupt
        }
    }

    /**
     * Gửi một email
     */
    private void sendEmail(EmailData emailData) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            setRecipients(message, emailData);

            message.setSubject(emailData.getSubject());

            // Tạo multipart (body + attachments)
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(createBodyPart(emailData));
            for (File file : emailData.getAttachments()) {
                multipart.addBodyPart(createAttachmentPart(file));
            }

            message.setContent(multipart);

            Transport.send(message); // Gửi email
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ------------------ Helper Methods ------------------

    private void setRecipients(Message message, EmailData emailData) throws MessagingException {
        if (!emailData.getTo().isEmpty())
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(String.join(",", emailData.getTo())));
        if (!emailData.getCc().isEmpty())
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(String.join(",", emailData.getCc())));
        if (!emailData.getBcc().isEmpty())
            message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(String.join(",", emailData.getBcc())));
    }

    private MimeBodyPart createBodyPart(EmailData emailData) throws MessagingException {
        MimeBodyPart bodyPart = new MimeBodyPart();
        if (emailData.isHtml()) {
            bodyPart.setContent(emailData.getContent(), "text/html; charset=utf-8");
        } else {
            bodyPart.setText(emailData.getContent());
        }
        return bodyPart;
    }

    private MimeBodyPart createAttachmentPart(File file) throws Exception {
        MimeBodyPart attachment = new MimeBodyPart();
        attachment.attachFile(file);
        return attachment;
    }
}