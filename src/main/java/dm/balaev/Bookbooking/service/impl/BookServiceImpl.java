package dm.balaev.Bookbooking.service.impl;

import dm.balaev.Bookbooking.exceptions.BookNotFoundException;
import dm.balaev.Bookbooking.payload.response.ApiResponse;
import dm.balaev.Bookbooking.persistance.entity.Book;
import dm.balaev.Bookbooking.persistance.repository.BookRepository;
import dm.balaev.Bookbooking.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    public Book addBook(Book book) {
        //TODO: добавить проверку на дубликаты
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Long id, Book book) {
        Book update = bookRepository.findById(id)
                .orElseThrow(()-> new BookNotFoundException("Book for update not found with id " + id));
        update.setAuthor(update.getAuthor());
        update.setTitle(update.getTitle());
        update.setCopiesAvailable(update.getCopiesAvailable());

        return bookRepository.save(update);
    }

    @Override
    public List<Book> getAllBook() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    @Override
    public Book findByName(String name) {
        return bookRepository.findByTitle(name)
                .orElseThrow(()-> new BookNotFoundException("Book for update not found with name " + name));
    }

    @Override
    public ApiResponse deleteBook(Long id) {
        bookRepository.deleteById(id);
        return new ApiResponse("You successfully delete book");
    }
}