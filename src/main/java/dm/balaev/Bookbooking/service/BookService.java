package dm.balaev.Bookbooking.service;

import dm.balaev.Bookbooking.payload.response.ApiResponse;
import dm.balaev.Bookbooking.persistance.entity.Book;

import java.util.List;

public interface BookService {
    Book addBook(Book book);
    Book updateBook(Long id, Book book);
    List<Book> getAllBook();
    List<Book> findByAuthor(String author);
    Book findByName(String name);
    ApiResponse deleteBook(Long id);

}
