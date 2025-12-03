# Hướng dẫn sử dụng thư viện GmailSender

## Giới thiệu

Thư viện `gmail-sender-1.0.0.jar` cung cấp các chức năng gửi email qua SMTP của Gmail, hỗ trợ cả chế độ đồng bộ (synchronous) và bất đồng bộ (asynchronous). Thư viện này sử dụng Jakarta Mail để thực hiện việc gửi email.

### Các lớp chính:
1. **GmailSender**: Lớp chính để gửi email.
2. **EmailData**: Lớp chứa thông tin của email như người nhận, chủ đề, nội dung và các file đính kèm.

## Cài đặt

1. Tải thư viện `gmail-sender-1.0.0.jar` và thêm vào classpath của dự án.
2. Đảm bảo rằng bạn đã cấu hình đúng Java và có kết nối mạng để gửi email qua SMTP của Gmail.

## Khởi tạo thư viện

Trước khi sử dụng thư viện để gửi email, bạn cần khởi tạo `GmailSender` với tài khoản Gmail và mật khẩu ứng dụng (App Password).

### Bước 1: Tạo mật khẩu ứng dụng (App Password)
Để sử dụng Gmail SMTP, bạn cần bật xác thực hai yếu tố cho tài khoản Google của mình và tạo mật khẩu ứng dụng. Bạn có thể tạo mật khẩu ứng dụng tại [Đây](https://support.google.com/accounts/answer/185833?hl=vi).

### Bước 2: Khởi tạo `GmailSender`

```java
import phongtt.mailsender.GmailSender;
import phongtt.mailsender.EmailData;

import java.io.File;
import java.util.Arrays;

public class EmailSenderExample {
    public static void main(String[] args) {
        // Khởi tạo GmailSender với tài khoản và mật khẩu ứng dụng
        String username = "your-email@gmail.com";  // Địa chỉ email Gmail của bạn
        String appPassword = "your-app-password";  // Mật khẩu ứng dụng của bạn

        // Khởi tạo GmailSender với chế độ bất đồng bộ (true)
        GmailSender sender = GmailSender.init(username, appPassword, true);

        // Khởi tạo EmailData chứa thông tin email
        EmailData email = new EmailData();
        email.setSubject("Test Email");
        email.setContent("Đây là email thử nghiệm với file đính kèm.");
        email.setHtml(false);  // false cho email dạng văn bản thuần túy, true cho email HTML

        // Danh sách người nhận (TO, CC, BCC)
        email.setTo(Arrays.asList("recipient1@example.com", "recipient2@example.com"));
        email.setCc(Arrays.asList("cc@example.com"));
        email.setBcc(Arrays.asList("bcc@example.com"));

        // Thêm file đính kèm (ví dụ: file PDF hoặc hình ảnh)
        File attachment = new File("path/to/your/attachment.pdf");
        email.addAttachment(attachment);

        // Gửi email (bất đồng bộ)
        sender.send(email);

        System.out.println("Email đang được gửi...");
    }
}
```
## Các phương thức chính

### 1. Khởi tạo `GmailSender`

`init(String username, String appPassword, boolean async):`

- **username**: Tài khoản Gmail của bạn.
- **appPassword**: Mật khẩu ứng dụng của bạn (không phải mật khẩu Gmail).
- **async**: Chế độ gửi email (true = bất đồng bộ, false = đồng bộ).

### 2. Gửi Email

`send(EmailData emailData):`

Phương thức này sẽ gửi email. Nếu chế độ là bất đồng bộ (`async = true`), email sẽ được thêm vào hàng đợi và gửi trong nền. Nếu chế độ đồng bộ (`async = false`), email sẽ được gửi ngay lập tức.

### 3. Lớp `EmailData`

`EmailData` là lớp chứa các thông tin về email cần gửi. Các thuộc tính của lớp `EmailData` bao gồm:

- **subject**: Tiêu đề email.
- **content**: Nội dung email (có thể là văn bản thuần túy hoặc HTML).
- **isHtml**: Cho biết email có phải là HTML không.
- **to**: Danh sách người nhận chính (TO).
- **cc**: Danh sách người nhận sao chép (CC).
- **bcc**: Danh sách người nhận sao chép ẩn (BCC).
- **attachments**: Danh sách các file đính kèm.

#### Các phương thức của `EmailData`:

- **setSubject(String subject)**: Thiết lập tiêu đề email.
- **setContent(String content)**: Thiết lập nội dung email.
- **setHtml(boolean isHtml)**: Thiết lập kiểu nội dung (true cho HTML, false cho văn bản thuần túy).
- **setTo(List<String> to)**: Thiết lập người nhận chính (TO).
- **setCc(List<String> cc)**: Thiết lập người nhận sao chép (CC).
- **setBcc(List<String> bcc)**: Thiết lập người nhận sao chép ẩn (BCC).
- **addAttachment(File file)**: Thêm file đính kèm vào email.

## Các lưu ý quan trọng

- **Mật khẩu ứng dụng (App Password)**: Đảm bảo rằng bạn đã sử dụng mật khẩu ứng dụng khi bật xác thực 2 yếu tố cho tài khoản Google của mình. Mật khẩu ứng dụng có thể được tạo tại phần cài đặt bảo mật trong tài khoản Google.

- **Chế độ gửi bất đồng bộ**: Nếu bạn chọn chế độ bất đồng bộ (`async = true`), email sẽ được gửi trong nền mà không làm gián đoạn tiến trình chính của chương trình.

- **Chế độ gửi đồng bộ**: Nếu bạn chọn chế độ đồng bộ (`async = false`), email sẽ được gửi ngay lập tức, và chương trình sẽ đợi cho đến khi email được gửi thành công.

## Câu hỏi thường gặp (FAQ)

### 1. Tôi không thể gửi email. Làm thế nào để khắc phục?

- Kiểm tra xem bạn có đang sử dụng đúng tài khoản Gmail và mật khẩu ứng dụng không.
- Đảm bảo rằng bạn đã bật xác thực 2 yếu tố cho tài khoản Google của mình và sử dụng mật khẩu ứng dụng.
- Kiểm tra kết nối mạng của bạn.

### 2. Làm thế nào để gửi email với nhiều người nhận?

- Bạn có thể thêm nhiều địa chỉ email vào các danh sách `to`, `cc`, hoặc `bcc`. Dữ liệu sẽ được tự động phân tách bởi dấu phẩy.

### 3. Làm sao để gửi email với nhiều tệp đính kèm?

- Bạn có thể sử dụng phương thức `addAttachment(File file)` của lớp `EmailData` để thêm nhiều tệp đính kèm vào email.
