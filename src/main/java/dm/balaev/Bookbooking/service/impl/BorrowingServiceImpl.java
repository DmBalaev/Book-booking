package dm.balaev.Bookbooking.service.impl;

import dm.balaev.Bookbooking.exceptions.*;
import dm.balaev.Bookbooking.payload.response.ApiResponse;
import dm.balaev.Bookbooking.persistance.entity.Account;
import dm.balaev.Bookbooking.persistance.entity.Book;
import dm.balaev.Bookbooking.persistance.entity.Borrowing;
import dm.balaev.Bookbooking.persistance.repository.AccountRepository;
import dm.balaev.Bookbooking.persistance.repository.BookRepository;
import dm.balaev.Bookbooking.persistance.repository.BorrowingRepository;
import dm.balaev.Bookbooking.service.BorrowingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class BorrowingServiceImpl implements BorrowingService {
    private final BorrowingRepository borrowingRepository;
    private final BookRepository bookRepository;
    private final AccountRepository accountRepository;

    @Override
    public List<Borrowing> getAllBorrowings() {
        return borrowingRepository.findAll();
    }

    @Override
    public Borrowing borrowBook(String email, Long bookId, LocalDate returnDate) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFound("User not found with email: " + email));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()-> new ResourceNotFound("Book for update not found with id: " + bookId));

        if (book.getCopiesAvailable() > 0){
            Borrowing borrowing = Borrowing.builder()
                    .account(account)
                    .book(book)
                    .borrowedDate(LocalDate.now())
                    .returnDate(returnDate)
                    .build();

            book.setCopiesAvailable(book.getCopiesAvailable() - 1);
            bookRepository.save(book);

            return borrowingRepository.save(borrowing);
        }else {
            throw new BookNotAvailableException("Book is not available for borrowing: " + bookId);
        }
    }

    @Override
    public Borrowing extendBorrowing(Long id, LocalDate date) {
        Borrowing borrowing = borrowingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Borrowing not found with id: " + id));
        if (borrowing.isExtended()){
            throw new BorrowingAlreadyExtendedException("Borrowing already extended");
        }

        borrowing.setReturnDate(date);
        borrowing.setExtended(true);

        return borrowingRepository.save(borrowing);
    }

    @Override
    public ApiResponse returnBook(Long id) {
        Borrowing borrowing = borrowingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Borrowing not found with id: " + id));

        Book book = borrowing.getBook();
        book.setCopiesAvailable(book.getCopiesAvailable() + 1);
        bookRepository.save(book);

        borrowing.setReturned(true);
        borrowingRepository.save(borrowing);

        return new ApiResponse("book returned");
    }
}
