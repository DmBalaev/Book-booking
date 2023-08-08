package dm.balaev.Bookbooking.service.impl;

import dm.balaev.Bookbooking.persistance.entity.Account;
import dm.balaev.Bookbooking.persistance.entity.Book;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailSenderService {
    private final JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);
    }

    public void notifyBorrowingExpiration(Account account, Book book) {
        String to = account.getEmail();
        String subject = "Reservation Expiration Reminder";
        String body = String.format("""
                        Dear %s,

                        As a reminder, your borrowing of the book '%s' by %s is about to expire. 
                        Please visit the library to return the book.""",
                account.getEmail(), book.getTitle(), book.getAuthor());

        sendEmail(to, subject, body);
    }

    public void notifyBookAvailability(Account account, Book book) {
        String to = account.getEmail();
        String subject = "Book Booking";
        String body = String.format("""
                        Dear %s,

                        Good news! The book '%s' by %s is now available to receive. 
                        Please visit the library to get the book.""",
                account.getEmail(), book.getTitle(), book.getAuthor());

        sendEmail(to, subject, body);
    }
}
