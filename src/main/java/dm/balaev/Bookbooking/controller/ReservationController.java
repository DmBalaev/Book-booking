package dm.balaev.Bookbooking.controller;


import dm.balaev.Bookbooking.payload.response.ApplicationResponse;
import dm.balaev.Bookbooking.persistance.entity.Reservation;
import dm.balaev.Bookbooking.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @Operation(
            summary = "Reserve a book",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Book reserved successfully",
                            content = @Content(mediaType = "application/json"))}
    )
    @PostMapping
    public ResponseEntity<Reservation> reserveBook(
            @Parameter(description = "ID of the book to reserve") @RequestParam Long bookId,
            @Parameter(description = "User's email") @RequestParam String email
    ) {
        log.info("Received request to reserve a book. Book ID: {}, Email: {}", bookId, email);
        Reservation reservation = reservationService.reserveBook(bookId, email);
        log.info("Book reserved successfully. Reservation details: {}", reservation);

        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @Operation(
            summary = "Cancel a book reservation",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Book reservation cancelled successfully",
                            content = @Content(mediaType = "application/json"))}
    )
    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApplicationResponse> cancelBookReservation(
            @Parameter(description = "Reservation ID to cancel") @PathVariable Long id
    ) {
        log.info("Received request to cancel book reservation with ID: {}", id);
        ApplicationResponse response = reservationService.cancelBookReservation(id);
        log.info("Book reservation cancellation response: {}", response);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get all reservations",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved all reservations",
                            content = @Content(mediaType = "application/json"))}
    )
    @GetMapping
    public ResponseEntity<List<Reservation>> allReservations() {
        log.info("Received request to fetch all reservations.");
        List<Reservation> reservations = reservationService.allReservations();
        log.info("Fetched {} reservations.", reservations.size());

        return ResponseEntity.ok(reservations);
    }

    @Operation(
            summary = "Get reservations by account",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved reservations by account",
                            content = @Content(mediaType = "application/json"))}
    )
    @GetMapping("/byAccount/{email}")
    public ResponseEntity<List<Reservation>> reservationByAccount(
            @Parameter(description = "User's email") @PathVariable String email
    ) {
        log.info("Received request to fetch reservations by account. Email: {}", email);
        List<Reservation> reservations = reservationService.reservationByAccount(email);
        log.info("Fetched {} reservations for account: {}", reservations.size(), email);

        return ResponseEntity.ok(reservations);
    }
}
