package dm.balaev.Bookbooking.persistance.repository;

import dm.balaev.Bookbooking.persistance.entity.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {
}
