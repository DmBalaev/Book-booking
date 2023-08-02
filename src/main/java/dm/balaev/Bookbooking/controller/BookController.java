package dm.balaev.Bookbooking.controller;

import dm.balaev.Bookbooking.payload.response.ApiResponse;
import dm.balaev.Bookbooking.persistance.entity.Book;
import dm.balaev.Bookbooking.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        log.info("Received request to add a new book: {}", book);
        Book newBook = bookService.addBook(book);
        log.info("Book added successfully: {}", newBook);

        return ResponseEntity.status(HttpStatus.CREATED).body(newBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        log.info("Received request to update book with ID {}: {}", id, book);
        Book updatedBook = bookService.updateBook(id, book);
        log.info("Book updated successfully: {}", updatedBook);

        return ResponseEntity.ok(updatedBook);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(@RequestParam(required = false) Long cursorId,
                                                  @RequestParam(defaultValue = "20") int size) {
        log.info("Received request to fetch all books.");
        Pageable pageable = PageRequest.of(0, size);
        List<Book> books = bookService.getAllBook(pageable, cursorId);
        log.info("Fetched {} books.", books.size());

        return ResponseEntity.ok(books);
    }

    @GetMapping("/byAuthor/{author}")
    public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable String author) {
        log.info("Received request to fetch books by author: {}", author);
        List<Book> books = bookService.findByAuthor(author);
        log.info("Fetched {} books for author: {}", books.size(), author);

        return ResponseEntity.ok(books);
    }

    @GetMapping("/byName/{name}")
    public ResponseEntity<Book> getBookByName(@PathVariable String name) {
        log.info("Received request to fetch book by name: {}", name);
        Book book = bookService.findByName(name);
        log.info("Fetched book by name {}: {}", name, book);

        return ResponseEntity.ok(book);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteBook(@PathVariable Long id) {
        log.info("Received request to delete book with ID: {}", id);
        ApiResponse response = bookService.deleteBook(id);
        log.info("Deletion response: {}", response);

        return ResponseEntity.ok(response);
    }
}
