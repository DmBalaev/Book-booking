package dm.balaev.Bookbooking.controller;

import dm.balaev.Bookbooking.payload.response.ApiResponse;
import dm.balaev.Bookbooking.persistance.entity.Borrowing;
import dm.balaev.Bookbooking.service.BorrowingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/borrowings")
public class BorrowingController {
    private final BorrowingService borrowingService;

    @GetMapping
    public ResponseEntity<List<Borrowing>> getAllBorrowings() {
        log.info("Received request to fetch all borrowings.");
        List<Borrowing> borrowings = borrowingService.getAllBorrowings();
        log.info("Fetched {} borrowings.", borrowings.size());

        return ResponseEntity.ok(borrowings);
    }

    @PostMapping
    public ResponseEntity<Borrowing> borrowBook(@RequestParam String email,
                                                @RequestParam Long bookId,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate) {
        log.info("Received request to borrow a book. Email: {}, Book ID: {}, Return Date: {}", email, bookId, returnDate);
        Borrowing borrowing = borrowingService.borrowBook(email, bookId, returnDate);
        log.info("Book borrowed successfully. Borrowing details: {}", borrowing);

        return ResponseEntity.status(HttpStatus.CREATED).body(borrowing);
    }

    @PostMapping("/{id}/extend")
    public ResponseEntity<Borrowing> extendBorrowing(@PathVariable Long id,
                                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Received request to extend borrowing with ID {}. New return date: {}", id, date);
        Borrowing extendedBorrowing = borrowingService.extendBorrowing(id, date);
        log.info("Borrowing extended successfully. Borrowing details: {}", extendedBorrowing);

        return ResponseEntity.ok(extendedBorrowing);
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<ApiResponse> returnBook(@PathVariable Long id) {
        log.info("Received request to return book with ID: {}", id);
        ApiResponse response = borrowingService.returnBook(id);
        log.info("Book return response: {}", response);

        return ResponseEntity.ok(response);
    }
}