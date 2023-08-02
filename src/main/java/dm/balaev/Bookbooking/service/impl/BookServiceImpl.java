package dm.balaev.Bookbooking.service.impl;

import dm.balaev.Bookbooking.exceptions.DuplicateException;
import dm.balaev.Bookbooking.exceptions.ResourceNotFound;
import dm.balaev.Bookbooking.payload.response.ApiResponse;
import dm.balaev.Bookbooking.persistance.entity.Book;
import dm.balaev.Bookbooking.persistance.repository.BookRepository;
import dm.balaev.Bookbooking.service.BookService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    @CacheEvict(value = "books")
    public Book addBook(Book book) {
        if (bookRepository.existsByTitleAndAuthor(book.getTitle(), book.getAuthor())){
            throw new DuplicateException("Book with title: '%s' and author: '%s' already exists."
                    .formatted(book.getTitle(), book.getAuthor()));
        }
        return bookRepository.save(book);
    }

    @Override
    @CacheEvict(value = "books")
    public Book updateBook(Long id, Book book) {
        Book update = bookRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFound("Book for update not found with id " + id));
        update.setAuthor(update.getAuthor());
        update.setTitle(update.getTitle());
        update.setCopiesAvailable(update.getCopiesAvailable());

        return bookRepository.save(update);
    }

    @Override
    @Cacheable("books")
    public List<Book> getAllBook() {
        return bookRepository.findAll();
    }

    @Override
    @Cacheable("booksByAuthor")
    public List<Book> findByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    @Override
    @Cacheable("booksByName")
    public Book findByName(String name) {
        return bookRepository.findByTitle(name)
                .orElseThrow(()-> new ResourceNotFound("Book for update not found with name " + name));
    }

    @Override
    @CacheEvict(value = "books", allEntries = true)
    public ApiResponse deleteBook(Long id) {
        bookRepository.deleteById(id);
        return new ApiResponse("You successfully delete book");
    }
}
