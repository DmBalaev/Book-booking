package dm.balaev.Bookbooking.persistance.repository;

import dm.balaev.Bookbooking.persistance.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
