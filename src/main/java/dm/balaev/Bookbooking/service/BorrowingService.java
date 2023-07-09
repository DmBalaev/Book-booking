package dm.balaev.Bookbooking.service;

import dm.balaev.Bookbooking.payload.response.ApiResponse;
import dm.balaev.Bookbooking.persistance.entity.Borrowing;

import java.time.LocalDate;
import java.util.List;

public interface BorrowingService {
    List<Borrowing> getAllBorrowings();
    Borrowing borrowBook(String email, Long bookId, LocalDate returnDate);
    Borrowing extendBorrowing(Long id, LocalDate date);
    ApiResponse returnBook(Long id);
}
