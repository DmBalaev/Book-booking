package dm.balaev.Bookbooking.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class BookNotAvailableException extends RuntimeException{
    public BookNotAvailableException(String message) {
        super(message);
    }
}
