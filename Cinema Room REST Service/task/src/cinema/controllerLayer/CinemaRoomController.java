package cinema.controllerLayer;

import cinema.dataLayer.*;
import cinema.errors.AlreadyPurchasedError;
import cinema.errors.InvalidPasswordError;
import cinema.errors.OutOfBoundsError;
import cinema.errors.WrongTokenError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class CinemaRoomController {

    private final String OUT_OF_BOUNDS_TEXT = "The number of a row or a column is out of bounds!";
    private final String ALREADY_PURCHASED_TEXT = "The ticket has been already purchased!";
    private final String WRONG_TOKEN_TEXT = "Wrong token!";
    private final int NUMBER_OF_ROWS = 9;
    private final int NUMBER_OF_COLUMNS = 9;
    private final int CHEAP_PRICE = 8;
    private final int EXPENSIVE_PRICE = 10;

    private final String INVALID_PASSWORD = "The password is wrong!";
    private final String SUPER_SECRET_PASSWORD = "super_secret";
    private CinemaRepresentation cinemaRepresentation = createCinemaRepresentation();

    private int income = 0;

    private int availableSeats = 81;
    private CinemaRepresentation createCinemaRepresentation() {

         CopyOnWriteArrayList seats = new CopyOnWriteArrayList();

        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                if(i < 5 ) {
                    seats.add(new Seat(i, j, false, EXPENSIVE_PRICE));
                } else {
                    seats.add(new Seat(i, j, false, CHEAP_PRICE));
                }
            }
        }
        return new CinemaRepresentation(NUMBER_OF_ROWS, NUMBER_OF_COLUMNS, seats);
    }

    @GetMapping("/seats")
    public CinemaRepresentation getMyCinema() {
        CopyOnWriteArrayList seats = new CopyOnWriteArrayList();
        for (Seat seat : cinemaRepresentation.getAvailableSeats()) {
            if (!seat.isBooked()) {
                seats.add(seat);

            }
        }
        return new CinemaRepresentation(NUMBER_OF_ROWS, NUMBER_OF_COLUMNS,seats);
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> postPurchase(@RequestBody PurchaseRequest request) {
        for (int i = 0; i < cinemaRepresentation.getAvailableSeats().size(); i++) {
            if(cinemaRepresentation.getAvailableSeats().get(i).getRow() == request.getRow()
                    && cinemaRepresentation.getAvailableSeats().get(i).getColumn() == request.getColumn()) {
                if (!cinemaRepresentation.getAvailableSeats().get(i).isBooked()) {
                    UUID uuid = UUID.randomUUID();
                    availableSeats--;
                    cinemaRepresentation.getAvailableSeats().get(i).setBooked(true);
                    cinemaRepresentation.getAvailableSeats().get(i).setToken(uuid);
                    if (request.getRow() < 5) {
                        income += EXPENSIVE_PRICE;
                        return new ResponseEntity<>(new PurchaseAnswer(uuid, new Ticket(request.getRow(), request.getColumn(), EXPENSIVE_PRICE)), HttpStatus.OK);
                    } else {
                        income += CHEAP_PRICE;
                        return new ResponseEntity<>(new PurchaseAnswer(uuid, new Ticket(request.getRow(), request.getColumn(), CHEAP_PRICE)), HttpStatus.OK);
                    }
                } else {
                   return new ResponseEntity<>(new AlreadyPurchasedError(HttpStatus.BAD_REQUEST.value(),ALREADY_PURCHASED_TEXT), HttpStatus.BAD_REQUEST);
                }
            }
        }
       return new ResponseEntity<>(new OutOfBoundsError(HttpStatus.BAD_REQUEST.value(), OUT_OF_BOUNDS_TEXT), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/return")
    public ResponseEntity<?> postReurn(@RequestBody ReturnRequest returnRequest) {
        for (int i = 0; i < cinemaRepresentation.getAvailableSeats().size(); i++) {
            Seat currentSeat = cinemaRepresentation.getAvailableSeats().get(i);
            Optional<UUID> uuidOptional = Optional.ofNullable(currentSeat.getToken());
            if (uuidOptional.isPresent() && returnRequest.getToken().toString().equals(uuidOptional.get().toString())) {
                currentSeat.setBooked(false);
                currentSeat.setToken(null);
                availableSeats++;
                income -= currentSeat.getPrice();
                return new ResponseEntity<>(new ReturnAnswer(new Ticket(currentSeat.getRow(), currentSeat.getColumn(), currentSeat.getPrice())),HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new WrongTokenError(WRONG_TOKEN_TEXT), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/stats")
    public ResponseEntity<?> postStats(@RequestParam(value = "password", required = false) String password) {
        Optional<String> passwordOptional = Optional.ofNullable(password);
        if (passwordOptional.isPresent() && passwordOptional.get().equals(SUPER_SECRET_PASSWORD)) {
            return new ResponseEntity<>(new StatsAnswer(income, availableSeats, 81 - availableSeats),HttpStatus.OK);
        }
        return new ResponseEntity<>(new InvalidPasswordError(INVALID_PASSWORD), HttpStatus.UNAUTHORIZED);
    }
}
