package dm.balaev.Bookbooking.exceptions;

import dm.balaev.Bookbooking.payload.response.ApplicationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotAvailableException.class)
    public ResponseEntity<ApplicationResponse> catchBookNotAvailableException(BookNotAvailableException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ApplicationResponse(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ApplicationResponse> catchResourceNotFound(ResourceNotFound e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ApplicationResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BorrowingAlreadyExtendedException.class)
    public ResponseEntity<ApplicationResponse> catchBorrowingAlreadyExtendedException(BorrowingAlreadyExtendedException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ApplicationResponse(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ApplicationResponse> catchDuplicateException(DuplicateException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ApplicationResponse(e.getMessage()), HttpStatus.CONFLICT);
    }
}
