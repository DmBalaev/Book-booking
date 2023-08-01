package dm.balaev.Bookbooking.service.impl;

import dm.balaev.Bookbooking.exceptions.ResourceNotFound;
import dm.balaev.Bookbooking.payload.response.ApiResponse;
import dm.balaev.Bookbooking.persistance.entity.Account;
import dm.balaev.Bookbooking.persistance.entity.Book;
import dm.balaev.Bookbooking.persistance.entity.Reservation;
import dm.balaev.Bookbooking.persistance.repository.AccountRepository;
import dm.balaev.Bookbooking.persistance.repository.BookRepository;
import dm.balaev.Bookbooking.persistance.repository.ReservationRepository;
import dm.balaev.Bookbooking.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final AccountRepository accountRepository;
    @Override
    public Reservation reserveBook(Long bookId, String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFound("User not found with email: " + email));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()-> new ResourceNotFound("Book for update not found with id: " + bookId));

        Reservation reservation = Reservation.builder()
                .account(account)
                .book(book)
                .reservationDate(LocalDate.now())
                .expirationDate(LocalDate.now().minusMonths(1))
                .build();
        return reservationRepository.save(reservation);
    }

    @Override
    public ApiResponse cancelBookReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFound("Reservation not found with id: " + id));

        reservationRepository.delete(reservation);
        return new ApiResponse("Reservation deleted");
    }

    @Override
    public List<Reservation> allReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public List<Reservation> reservationByAccount(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFound("User not found with email: " + email));
        //TODO: make method
        return reservationRepository.findAll();
    }
}
