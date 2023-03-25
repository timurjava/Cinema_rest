package cinema.dataLayer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties({"booked", "token"})
public class Seat {

    private int row;
    private int column;
    private boolean isBooked;
    private int price;

    private UUID token;

    public Seat(int row, int column, boolean isBooked, int price) {
        this.row = row;
        this.column = column;
        this.isBooked = isBooked;
        this.price = price;
    }
}
