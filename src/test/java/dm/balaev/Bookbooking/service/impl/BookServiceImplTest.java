package dm.balaev.Bookbooking.service.impl;

import dm.balaev.Bookbooking.exceptions.DuplicateException;
import dm.balaev.Bookbooking.payload.response.ApiResponse;
import dm.balaev.Bookbooking.persistance.entity.Book;
import dm.balaev.Bookbooking.persistance.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookServiceImpl bookService;
    private Book book;

    @BeforeEach
    public void init(){
         book = Book.builder()
                .id(1L)
                .title("Same Book")
                .author("Same Author")
                .copiesAvailable(5)
                .build();
    }

    @Test
    void testAddBook() {
        when(bookRepository.existsByTitleAndAuthor(anyString(), anyString())).thenReturn(false);
        when(bookRepository.save(book)).thenReturn(book);

        Book addedBook = bookService.addBook(book);

        verify(bookRepository, times(1)).existsByTitleAndAuthor(anyString(), anyString());
        verify(bookRepository, times(1)).save(book);
        assertNotNull(addedBook);
        assertEquals("Same Book", addedBook.getTitle());
        assertEquals("Same Author", addedBook.getAuthor());
        assertEquals(5, addedBook.getCopiesAvailable());
    }

    @Test
    void testAddBookWithDuplicate() {
        when(bookRepository.existsByTitleAndAuthor(anyString(), anyString())).thenReturn(true);

        assertThrows(DuplicateException.class, () -> bookService.addBook(book));

        verify(bookRepository, times(1)).existsByTitleAndAuthor(anyString(), anyString());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void testUpdateBook() {
        Book updatedBook = Book.builder()
                .id(1L)
                .title("Updated Book")
                .author("Updated Author")
                .copiesAvailable(10)
                .build();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(new Book(1L, "Sample Book", "Sample Author", 5)));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        Book result = bookService.updateBook(1L, updatedBook);

        assertEquals(updatedBook, result);
    }

    @Test
    void testGetAllBook() {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "Book 1", "Author 1", 5));
        books.add(new Book(2L, "Book 2", "Author 2", 3));

        when(bookRepository.findByIdLessThanOrderByIdDesc(anyLong(), any(Pageable.class)))
                .thenReturn(books);

        int page = 0;
        int pageSize = 10;
        Long cursorId = 3L;
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        List<Book> result = bookService.getAllBook(pageable, cursorId);

        assertEquals(2, result.size());
        assertEquals(books.get(0), result.get(0));
        assertEquals(books.get(1), result.get(1));

        verify(bookRepository, times(1)).findByIdLessThanOrderByIdDesc(cursorId, pageable);
        verify(bookRepository, times(1)).findByIdLessThanOrderByIdDesc(cursorId, pageable);
    }

    @Test
    void testFindByAuthor() {
        String author = book.getAuthor();
        List<Book> booksByAuthor = new ArrayList<>();
        booksByAuthor.add(new Book(1L, "Book 1", author, 5));
        booksByAuthor.add(new Book(2L, "Book 2", author, 3));

        when(bookRepository.findByAuthor(author)).thenReturn(booksByAuthor);

        List<Book> result = bookService.findByAuthor(author);

        verify(bookRepository, times(1)).findByAuthor(author);

        assertEquals(2, result.size());
        assertEquals("Book 1", result.get(0).getTitle());
        assertEquals(author, result.get(0).getAuthor());
        assertEquals(5, result.get(0).getCopiesAvailable());
        assertEquals("Book 2", result.get(1).getTitle());
        assertEquals(author, result.get(1).getAuthor());
        assertEquals(3, result.get(1).getCopiesAvailable());
    }

    @Test
    void testFindByName() {
        String bookName = book.getTitle();

        when(bookRepository.findByTitle(bookName)).thenReturn(Optional.of(book));

        Book result = bookService.findByName(bookName);

        verify(bookRepository, times(1)).findByTitle(bookName);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(bookName, result.getTitle());
        assertEquals("Same Author", result.getAuthor());
        assertEquals(5, result.getCopiesAvailable());
    }

    @Test
    void testDeleteBook() {
        Long bookId = 1L;

        ApiResponse result = bookService.deleteBook(bookId);

        verify(bookRepository, times(1)).deleteById(bookId);
        assertNotNull(result);
        assertEquals("You successfully delete book", result.getMessage());
    }
}