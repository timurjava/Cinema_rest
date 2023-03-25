package cinema.errors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OutOfBoundsError implements AppError {

    private int statusCode;
    private String error;
}
