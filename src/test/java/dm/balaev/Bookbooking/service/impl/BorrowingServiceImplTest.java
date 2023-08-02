package dm.balaev.Bookbooking.service.impl;

import dm.balaev.Bookbooking.exceptions.BookNotAvailableException;
import dm.balaev.Bookbooking.exceptions.BorrowingAlreadyExtendedException;
import dm.balaev.Bookbooking.exceptions.ResourceNotFound;
import dm.balaev.Bookbooking.payload.response.ApiResponse;
import dm.balaev.Bookbooking.persistance.entity.Account;
import dm.balaev.Bookbooking.persistance.entity.Book;
import dm.balaev.Bookbooking.persistance.entity.Borrowing;
import dm.balaev.Bookbooking.persistance.repository.AccountRepository;
import dm.balaev.Bookbooking.persistance.repository.BookRepository;
import dm.balaev.Bookbooking.persistance.repository.BorrowingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class BorrowingServiceImplTest {
    @Mock
    private BorrowingRepository borrowingRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private BorrowingServiceImpl borrowingService;
    private Book book;
    private Account account;
    private final String email = "user@example.com";
    private final Long bookId = 1L;
    private final Long borrowingId = 1L;

    @BeforeEach
    void setUp() {
        book = Book.builder()
                .id(1L)
                .title("Same Book")
                .author("Same Author")
                .copiesAvailable(5)
                .build();

        account = Account.builder()
                .id(1L)
                .name("User")
                .email("user@example.com")
                .build();

        LocalDate newReturnDate = LocalDate.now().plusDays(7);
    }

    @Test
    void testGetAllBorrowings() {
        List<Borrowing> borrowings = new ArrayList<>();
        borrowings.add(new Borrowing(1L, new Account(), new Book(), LocalDate.now(), LocalDate.now().plusDays(14), false, false));

        when(borrowingRepository.findAll()).thenReturn(borrowings);

        List<Borrowing> result = borrowingService.getAllBorrowings();

        verify(borrowingRepository).findAll();

        assertNotNull(result);
        assertEquals(borrowings.size(), result.size());
        assertEquals(borrowings.get(0), result.get(0));
    }

    @Test
    void testBorrowBook() {
        LocalDate returnDate = LocalDate.now().plusDays(14);

        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(borrowingRepository.save(any(Borrowing.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Borrowing result = borrowingService.borrowBook(email, bookId, returnDate);

        verify(accountRepository).findByEmail(email);
        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(book);
        verify(borrowingRepository).save(any(Borrowing.class));

        assertNotNull(result);
        assertEquals(account, result.getAccount());
        assertEquals(book, result.getBook());
        assertEquals(LocalDate.now(), result.getBorrowedDate());
        assertEquals(returnDate, result.getReturnDate());
    }

    @Test
    void testBorrowBookUserNotFound() {
        LocalDate returnDate = LocalDate.now().plusDays(14);

        when(accountRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, () -> borrowingService.borrowBook(email, bookId, returnDate));

        verify(accountRepository).findByEmail(email);
        verifyNoMoreInteractions(bookRepository, borrowingRepository);
    }

    @Test
    void testBorrowBookBookNotFound() {
        LocalDate returnDate = LocalDate.now().plusDays(14);

        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(new Account()));
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, () -> borrowingService.borrowBook(email, bookId, returnDate));

        verify(accountRepository).findByEmail(email);
        verify(bookRepository).findById(bookId);
        verifyNoMoreInteractions(borrowingRepository);
    }

    @Test
    void testBorrowBookNotAvailable() {
        LocalDate returnDate = LocalDate.now().plusDays(14);
        book.setCopiesAvailable(0);

        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        assertThrows(BookNotAvailableException.class, () -> borrowingService.borrowBook(email, bookId, returnDate));

        verify(accountRepository).findByEmail(email);
        verify(bookRepository).findById(bookId);
        verifyNoMoreInteractions(borrowingRepository);
    }

    @Test
    void testExtendBorrowing() {
        LocalDate newReturnDate = LocalDate.now().plusDays(7);
        Borrowing borrowing = new Borrowing(borrowingId, new Account(), new Book(), LocalDate.now(), LocalDate.now().plusDays(14), false, false);

        when(borrowingRepository.findById(borrowingId)).thenReturn(Optional.of(borrowing));
        when(borrowingRepository.save(any(Borrowing.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Borrowing result = borrowingService.extendBorrowing(borrowingId, newReturnDate);

        verify(borrowingRepository).findById(borrowingId);
        verify(borrowingRepository).save(any(Borrowing.class));

        assertNotNull(result);
        assertEquals(newReturnDate, result.getReturnDate());
        assertTrue(result.isExtended());
    }

    @Test
    void testExtendBorrowingNotFound() {
        LocalDate newReturnDate = LocalDate.now().plusDays(7);

        when(borrowingRepository.findById(borrowingId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, () -> borrowingService.extendBorrowing(borrowingId, newReturnDate));

        verify(borrowingRepository).findById(borrowingId);
        verifyNoMoreInteractions(borrowingRepository);
    }

    @Test
    void testExtendBorrowingAlreadyExtended() {
        LocalDate newReturnDate = LocalDate.now().plusDays(7);
        Borrowing borrowing = new Borrowing(borrowingId, new Account(), new Book(), LocalDate.now(), LocalDate.now().plusDays(14), true, true);

        when(borrowingRepository.findById(borrowingId)).thenReturn(Optional.of(borrowing));

        assertThrows(BorrowingAlreadyExtendedException.class, () -> borrowingService.extendBorrowing(borrowingId, newReturnDate));

        verify(borrowingRepository).findById(borrowingId);
        verifyNoMoreInteractions(borrowingRepository);
    }

    @Test
    void testReturnBook() {
        Borrowing borrowing = new Borrowing(borrowingId, new Account(), new Book(), LocalDate.now(), LocalDate.now().plusDays(14), false, false);

        when(borrowingRepository.findById(borrowingId)).thenReturn(Optional.of(borrowing));
        when(borrowingRepository.save(any(Borrowing.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ApiResponse result = borrowingService.returnBook(borrowingId);

        verify(borrowingRepository).findById(borrowingId);
        verify(borrowingRepository).save(any(Borrowing.class));

        assertNotNull(result);
        assertEquals("book returned", result.getMessage());
    }

    @Test
    void testReturnBookNotFound() {
        when(borrowingRepository.findById(borrowingId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, () -> borrowingService.returnBook(borrowingId));

        verify(borrowingRepository).findById(borrowingId);
        verifyNoMoreInteractions(borrowingRepository);
    }
}