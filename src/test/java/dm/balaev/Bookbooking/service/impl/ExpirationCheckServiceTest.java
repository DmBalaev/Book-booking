package dm.balaev.Bookbooking.service.impl;

import dm.balaev.Bookbooking.persistance.entity.Borrowing;
import dm.balaev.Bookbooking.persistance.entity.Reservation;
import dm.balaev.Bookbooking.service.BorrowingService;
import dm.balaev.Bookbooking.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ExpirationCheckServiceTest {
    @Mock
    private BorrowingService borrowingService;

    @Mock
    private ReservationService reservationService;

    @Mock
    private EmailSenderService emailSenderService;

    @InjectMocks
    private ExpirationCheckService expirationCheckService;

    @Test
    void testCheckBorrowingExpiration() {
        List<Borrowing> expiringBorrowings = new ArrayList<>();
        Borrowing borrowing1 = new Borrowing();
        Borrowing borrowing2 = new Borrowing();
        expiringBorrowings.add(borrowing1);
        expiringBorrowings.add(borrowing2);

        when(borrowingService.getExpiringBorrowing()).thenReturn(expiringBorrowings);

        expirationCheckService.checkBorrowingExpiration();

        verify(emailSenderService, times(2)).notifyBorrowingExpiration(any(), any());
    }

    @Test
    void testCheckBookAvailability() {
        List<Reservation> availableReservations = new ArrayList<>();
        Reservation reservation1 = new Reservation();
        Reservation reservation2 = new Reservation();
        availableReservations.add(reservation1);
        availableReservations.add(reservation2);

        when(reservationService.getAvailableBookedBooks()).thenReturn(availableReservations);

        expirationCheckService.checkBookAvailability();

        verify(emailSenderService, times(2)).notifyBookAvailability(any(), any());
    }
}