package dm.balaev.Bookbooking.controller;


import dm.balaev.Bookbooking.payload.response.ApiResponse;
import dm.balaev.Bookbooking.persistance.entity.Reservation;
import dm.balaev.Bookbooking.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Reservation> reserveBook(@RequestParam Long bookId,
                                                   @RequestParam String email) {
        Reservation reservation = reservationService.reserveBook(bookId, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse> cancelBookReservation(@PathVariable Long id) {
        ApiResponse response = reservationService.cancelBookReservation(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> allReservations() {
        List<Reservation> reservations = reservationService.allReservations();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/byAccount/{email}")
    public ResponseEntity<List<Reservation>> reservationByAccount(@PathVariable String email) {
        List<Reservation> reservations = reservationService.reservationByAccount(email);
        return ResponseEntity.ok(reservations);
    }
}
