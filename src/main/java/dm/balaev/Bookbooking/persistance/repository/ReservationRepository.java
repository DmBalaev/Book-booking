package dm.balaev.Bookbooking.persistance.repository;

import dm.balaev.Bookbooking.persistance.entity.Account;
import dm.balaev.Bookbooking.persistance.entity.Book;
import dm.balaev.Bookbooking.persistance.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByAccount(Account account);
    @Query("SELECT r FROM Reservation r JOIN r.book b WHERE b.copiesAvailable > 0")
    List<Reservation> findReservationsForAvailableBooks();
}
