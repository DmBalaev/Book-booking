package dm.balaev.Bookbooking.controller;

import dm.balaev.Bookbooking.payload.response.ApplicationResponse;
import dm.balaev.Bookbooking.persistance.entity.Borrowing;
import dm.balaev.Bookbooking.service.BorrowingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(
            summary = "Get all borrowings",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved all borrowings",
                            content = @Content(mediaType = "application/json"))}
    )
    @GetMapping
    public ResponseEntity<List<Borrowing>> getAllBorrowings() {
        log.info("Received request to fetch all borrowings.");
        List<Borrowing> borrowings = borrowingService.getAllBorrowings();
        log.info("Fetched {} borrowings.", borrowings.size());

        return ResponseEntity.ok(borrowings);
    }

    @Operation(
            summary = "Borrow a book",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Book borrowed successfully",
                            content = @Content(mediaType = "application/json"))}
    )
    @PostMapping
    public ResponseEntity<Borrowing> borrowBook(
            @Parameter(description = "User's email") @RequestParam String email,
            @Parameter(description = "Book ID to borrow") @RequestParam Long bookId,
            @Parameter(description = "Return date for the borrowed book") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate
    ) {
        log.info("Received request to borrow a book. Email: {}, Book ID: {}, Return Date: {}", email, bookId, returnDate);
        Borrowing borrowing = borrowingService.borrowBook(email, bookId, returnDate);
        log.info("Book borrowed successfully. Borrowing details: {}", borrowing);

        return ResponseEntity.status(HttpStatus.CREATED).body(borrowing);
    }

    @Operation(
            summary = "Extend borrowing period",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Borrowing extended successfully",
                            content = @Content(mediaType = "application/json"))}
    )
    @PostMapping("/{id}/extend")
    public ResponseEntity<Borrowing> extendBorrowing(
            @Parameter(description = "Borrowing ID to extend") @PathVariable Long id,
            @Parameter(description = "New return date for the extended borrowing") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        log.info("Received request to extending with ID {}. New return date: {}", id, date);
        Borrowing extendedBorrowing = borrowingService.extendBorrowing(id, date);
        log.info("Borrowing extended successfully. Borrowing details: {}", extendedBorrowing);

        return ResponseEntity.ok(extendedBorrowing);
    }

    @Operation(
            summary = "Return a borrowed book",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Book returned successfully",
                            content = @Content(mediaType = "application/json"))}
    )
    @PostMapping("/{id}/return")
    public ResponseEntity<ApplicationResponse> returnBook(
            @Parameter(description = "Borrowing ID of the book to return") @PathVariable Long id) {
        log.info("Received request to return book with ID: {}", id);
        ApplicationResponse response = borrowingService.returnBook(id);
        log.info("Book return response: {}", response);

        return ResponseEntity.ok(response);
    }
}