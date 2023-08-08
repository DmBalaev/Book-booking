package dm.balaev.Bookbooking.service.impl;

import dm.balaev.Bookbooking.persistance.entity.Account;
import dm.balaev.Bookbooking.persistance.entity.Book;
import dm.balaev.Bookbooking.persistance.entity.Borrowing;
import dm.balaev.Bookbooking.persistance.entity.Reservation;
import dm.balaev.Bookbooking.service.BorrowingService;
import dm.balaev.Bookbooking.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ExpirationCheckService {
    private final BorrowingService borrowingService;
    private final ReservationService reservationService;
    private final EmailSenderService emailSenderService;

    @Scheduled(fixedDelay = 86400000)
    public void checkBorrowingExpiration(){
        List<Borrowing> expiringReservations = borrowingService.getExpiringBorrowing();

        for (Borrowing borrowing : expiringReservations) {
            Account account = borrowing.getAccount();
            Book book = borrowing.getBook();
            emailSenderService.notifyBorrowingExpiration(account, book);
        }
    }

    @Scheduled(fixedDelay = 86400000)
    public void checkBookAvailability(){
        List<Reservation> expiringReservations = reservationService.getAvailableBookedBooks();

        for (Reservation reservation : expiringReservations) {
            Account account = reservation.getAccount();
            Book book = reservation.getBook();
            emailSenderService.notifyBookAvailability(account, book);
        }
    }
}
