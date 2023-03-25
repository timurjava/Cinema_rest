package cinema.errors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlreadyPurchasedError implements AppError {

    private int statusCode;
    private String error;
}
