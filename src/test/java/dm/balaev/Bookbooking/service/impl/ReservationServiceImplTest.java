package dm.balaev.Bookbooking.service.impl;

import dm.balaev.Bookbooking.exceptions.ResourceNotFound;
import dm.balaev.Bookbooking.payload.response.ApplicationResponse;
import dm.balaev.Bookbooking.persistance.entity.Account;
import dm.balaev.Bookbooking.persistance.entity.Book;
import dm.balaev.Bookbooking.persistance.entity.Reservation;
import dm.balaev.Bookbooking.persistance.repository.AccountRepository;
import dm.balaev.Bookbooking.persistance.repository.BookRepository;
import dm.balaev.Bookbooking.persistance.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ReservationServiceImplTest {
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private ReservationServiceImpl reservationService;
    private Book book;
    private Account account;
    private final String email = "user@example.com";
    private final Long bookId = 1L;

    @BeforeEach
    public void init(){
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
    void testReserveBook() {
        LocalDate now = LocalDate.now();
        LocalDate expirationDate = now.minusMonths(1);

        Reservation reservation = new Reservation(1L, account, book, now, expirationDate);

        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation result = reservationService.reserveBook(bookId, email);

        verify(accountRepository).findByEmail(email);
        verify(bookRepository).findById(bookId);
        verify(reservationRepository).save(any(Reservation.class));

        assertNotNull(result);
        assertEquals(account, result.getAccount());
        assertEquals(book, result.getBook());
        assertEquals(now, result.getReservationDate());
        assertEquals(expirationDate, result.getExpirationDate());
    }

    @Test
    void testReserveBookUserNotFound() {
        when(accountRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, () -> reservationService.reserveBook(bookId, email));

        verify(accountRepository).findByEmail(email);
        verifyNoMoreInteractions(bookRepository, reservationRepository);
    }

    @Test
    void testReserveBookBookNotFound() {
        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(new Account()));
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, () -> reservationService.reserveBook(bookId, email));

        verify(accountRepository).findByEmail(email);
        verify(bookRepository).findById(bookId);
        verifyNoMoreInteractions(reservationRepository);
    }

    @Test
    void testCancelBookReservation() {
        Long reservationId = 1L;
        Reservation reservation = new Reservation(reservationId, new Account(), new Book(), LocalDate.now(), LocalDate.now().minusMonths(1));

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        ApplicationResponse response = reservationService.cancelBookReservation(reservationId);

        verify(reservationRepository).findById(reservationId);
        verify(reservationRepository).delete(reservation);

        assertNotNull(response);
        assertEquals("Reservation deleted", response.getMessage());
    }

    @Test
    void testCancelBookReservationNotFound() {
        Long reservationId = 1L;

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, () -> reservationService.cancelBookReservation(reservationId));

        verify(reservationRepository).findById(reservationId);
        verifyNoMoreInteractions(reservationRepository);
    }

    @Test
    void testAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation(1L, new Account(), new Book(), LocalDate.now(), LocalDate.now().minusMonths(1)));

        when(reservationRepository.findAll()).thenReturn(reservations);

        List<Reservation> result = reservationService.allReservations();

        verify(reservationRepository).findAll();

        assertNotNull(result);
        assertEquals(reservations.size(), result.size());
        assertEquals(reservations.get(0), result.get(0));
    }

    @Test
    void testReservationByAccount() {
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation(1L, account, new Book(), LocalDate.now(), LocalDate.now().minusMonths(1)));

        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));
        when(reservationRepository.findByAccount(account)).thenReturn(reservations);

        List<Reservation> result = reservationService.reservationByAccount(email);

        verify(accountRepository).findByEmail(email);
        verify(reservationRepository).findByAccount(account);

        assertNotNull(result);
        assertEquals(reservations.size(), result.size());
        assertEquals(reservations.get(0), result.get(0));
    }

    @Test
    void testReservationByAccountUserNotFound() {
        when(accountRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, () -> reservationService.reservationByAccount(email));

        verify(accountRepository).findByEmail(email);
        verifyNoMoreInteractions(reservationRepository);
    }
}