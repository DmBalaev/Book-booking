package dm.balaev.Bookbooking.service;

import dm.balaev.Bookbooking.payload.response.ApplicationResponse;
import dm.balaev.Bookbooking.persistance.entity.Reservation;

import java.util.List;

public interface ReservationService {
    Reservation reserveBook(Long bookId, String email);
    ApplicationResponse cancelBookReservation(Long id);
    List<Reservation> allReservations();
    List<Reservation> reservationByAccount(String email);
    List<Reservation> getAvailableBookedBooks();
}
