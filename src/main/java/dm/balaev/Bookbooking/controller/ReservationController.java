package dm.balaev.Bookbooking.controller;


import dm.balaev.Bookbooking.payload.response.ApiResponse;
import dm.balaev.Bookbooking.persistance.entity.Reservation;
import dm.balaev.Bookbooking.service.ReservationService;
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

    @PostMapping
    public ResponseEntity<Reservation> reserveBook(@RequestParam Long bookId,
                                                   @RequestParam String email) {
        log.info("Received request to reserve a book. Book ID: {}, Email: {}", bookId, email);
        Reservation reservation = reservationService.reserveBook(bookId, email);
        log.info("Book reserved successfully. Reservation details: {}", reservation);

        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse> cancelBookReservation(@PathVariable Long id) {
        log.info("Received request to cancel book reservation with ID: {}", id);
        ApiResponse response = reservationService.cancelBookReservation(id);
        log.info("Book reservation cancellation response: {}", response);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> allReservations() {
        log.info("Received request to fetch all reservations.");
        List<Reservation> reservations = reservationService.allReservations();
        log.info("Fetched {} reservations.", reservations.size());

        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/byAccount/{email}")
    public ResponseEntity<List<Reservation>> reservationByAccount(@PathVariable String email) {
        log.info("Received request to fetch reservations by account. Email: {}", email);
        List<Reservation> reservations = reservationService.reservationByAccount(email);
        log.info("Fetched {} reservations for account: {}", reservations.size(), email);

        return ResponseEntity.ok(reservations);
    }
}
