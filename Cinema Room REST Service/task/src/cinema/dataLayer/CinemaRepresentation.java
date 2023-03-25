package cinema.dataLayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CinemaRepresentation {

    private int totalRows;
    private int totalColumns;
    private List<Seat> availableSeats;


}
