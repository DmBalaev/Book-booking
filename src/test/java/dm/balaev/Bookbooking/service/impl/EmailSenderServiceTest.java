package dm.balaev.Bookbooking.service.impl;

import dm.balaev.Bookbooking.persistance.entity.Account;
import dm.balaev.Bookbooking.persistance.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class EmailSenderServiceTest {
    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailSenderService emailSenderService;

    private Book book;
    private Account account;

    @BeforeEach
    void setUp() {
        book = Book.builder()
                .id(1L)
                .title("Same Book")
                .author("Same Author")
                .copiesAvailable(5)
                .build();

        account = Account.builder()
                .id(1L)
                .name("User")
                .email("user@example.com")
                .build();
    }

    @Test
    void testSendEmail() {
        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        emailSenderService.sendEmail("to@example.com", "Test Subject", "Test Body");

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testNotifyBorrowingExpiration() {
        emailSenderService.notifyBorrowingExpiration(account, book);

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testNotifyBookAvailability() {
        emailSenderService.notifyBookAvailability(account, book);

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}