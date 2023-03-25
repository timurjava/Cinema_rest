package cinema.dataLayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatsAnswer {

    private int current_income;
    private int number_of_available_seats;
    private int number_of_purchased_tickets;
}
