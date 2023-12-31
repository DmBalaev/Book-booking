package dm.balaev.Bookbooking.service;

import dm.balaev.Bookbooking.payload.response.ApplicationResponse;
import dm.balaev.Bookbooking.persistance.entity.Book;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
    Book addBook(Book book);
    Book updateBook(Long id, Book book);
    List<Book> getAllBook(Pageable pageable, Long cursorId);
    List<Book> findByAuthor(String author);
    Book findByName(String name);
    ApplicationResponse deleteBook(Long id);
}
