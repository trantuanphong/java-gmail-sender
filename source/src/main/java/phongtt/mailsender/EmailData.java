package phongtt.mailsender;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author PhongTT
 */
public class EmailData {

    // Các trường bắt buộc
    private List<String> to;        // Danh sách người nhận
    private String subject;         // Chủ đề email
    private String content;         // Nội dung email

    // Các trường tùy chọn
    private boolean html = true;   // Có phải nội dung HTML không
    private List<String> cc = new ArrayList<>();
    private List<String> bcc = new ArrayList<>();
    private List<File> attachments = new ArrayList<>();

    /**
     * Constructor với các trường bắt buộc
     */
    public EmailData(List<String> to, String subject, String content) {
        if (to == null || to.isEmpty())
            throw new IllegalArgumentException("Phải có ít nhất một người nhận (to)");
        if (subject == null || subject.isEmpty())
            throw new IllegalArgumentException("Subject là bắt buộc");
        if (content == null || content.isEmpty())
            throw new IllegalArgumentException("Content là bắt buộc");

        this.to = to;
        this.subject = subject;
        this.content = content;
    }
    
    // Getters
    public List<String> getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public boolean isHtml() {
        return html;
    }

    public List<String> getCc() {
        return cc;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public List<File> getAttachments() {
        return attachments;
    }

    // Setters for optional fields
    public EmailData setHtml(boolean html) {
        this.html = html;
        return this;
    }

    public EmailData setCc(List<String> cc) {
        this.cc = cc != null ? cc : new ArrayList<>();
        return this;
    }

    public EmailData setBcc(List<String> bcc) {
        this.bcc = bcc != null ? bcc : new ArrayList<>();
        return this;
    }

    public EmailData setAttachments(List<File> attachments) {
        this.attachments = attachments != null ? attachments : new ArrayList<>();
        return this;
    }
}
