package dm.balaev.Bookbooking.controller;

import dm.balaev.Bookbooking.payload.response.ApiResponse;
import dm.balaev.Bookbooking.persistance.entity.Borrowing;
import dm.balaev.Bookbooking.service.BorrowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/borrowings")
public class BorrowingController {
    private final BorrowingService borrowingService;

    @GetMapping
    public ResponseEntity<List<Borrowing>> getAllBorrowings() {
        List<Borrowing> borrowings = borrowingService.getAllBorrowings();
        return ResponseEntity.ok(borrowings);
    }

    @PostMapping
    public ResponseEntity<Borrowing> borrowBook(@RequestParam String email,
                                                @RequestParam Long bookId,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate) {
        Borrowing borrowing = borrowingService.borrowBook(email, bookId, returnDate);
        return ResponseEntity.status(HttpStatus.CREATED).body(borrowing);
    }

    @PostMapping("/{id}/extend")
    public ResponseEntity<Borrowing> extendBorrowing(@PathVariable Long id,
                                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Borrowing extendedBorrowing = borrowingService.extendBorrowing(id, date);
        return ResponseEntity.ok(extendedBorrowing);
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<ApiResponse> returnBook(@PathVariable Long id) {
        ApiResponse response = borrowingService.returnBook(id);
        return ResponseEntity.ok(response);
    }
}