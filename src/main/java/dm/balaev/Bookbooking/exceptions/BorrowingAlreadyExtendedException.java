package dm.balaev.Bookbooking.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class BorrowingAlreadyExtendedException extends RuntimeException{
    public BorrowingAlreadyExtendedException(String message) {
        super(message);
    }
}
