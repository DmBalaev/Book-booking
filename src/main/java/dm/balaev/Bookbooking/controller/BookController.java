package dm.balaev.Bookbooking.controller;

import dm.balaev.Bookbooking.payload.response.ApplicationResponse;
import dm.balaev.Bookbooking.persistance.entity.Book;
import dm.balaev.Bookbooking.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "Add a new book",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Book added successfully",
                            content = @Content(mediaType = "application/json"))}
    )
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        log.info("Received request to add a new book: {}", book);
        Book newBook = bookService.addBook(book);
        log.info("Book added successfully: {}", newBook);

        return ResponseEntity.status(HttpStatus.CREATED).body(newBook);
    }

    @Operation(
            summary = "Update a book by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Book updated successfully",
                            content = @Content(mediaType = "application/json"))}
    )
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        log.info("Received request to update book with ID {}: {}", id, book);
        Book updatedBook = bookService.updateBook(id, book);
        log.info("Book updated successfully: {}", updatedBook);

        return ResponseEntity.ok(updatedBook);
    }

    @Operation(
            summary = "Get all books",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved all books",
                            content = @Content(mediaType = "application/json"))}
    )
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(
            @Parameter(description = "Cursor ID for pagination") @RequestParam(required = false) Long cursorId,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "20") int size) {
        log.info("Received request to fetch all books.");
        Pageable pageable = PageRequest.of(0, size);
        List<Book> books = bookService.getAllBook(pageable, cursorId);
        log.info("Fetched {} books.", books.size());

        return ResponseEntity.ok(books);
    }

    @Operation(
            summary = "Get books by author",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved books by author",
                            content = @Content(mediaType = "application/json"))}
    )
    @GetMapping("/byAuthor/{author}")
    public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable String author) {
        log.info("Received request to fetch books by author: {}", author);
        List<Book> books = bookService.findByAuthor(author);
        log.info("Fetched {} books for author: {}", books.size(), author);

        return ResponseEntity.ok(books);
    }

    @Operation(
            summary = "Get book by name",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved book by name",
                            content = @Content(mediaType = "application/json"))}
    )
    @GetMapping("/byName/{name}")
    public ResponseEntity<Book> getBookByName(@PathVariable String name) {
        log.info("Received request to fetch book by name: {}", name);
        Book book = bookService.findByName(name);
        log.info("Fetched book by name {}: {}", name, book);

        return ResponseEntity.ok(book);
    }

    @Operation(
            summary = "Delete a book by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Book deleted successfully",
                            content = @Content(mediaType = "application/json"))}
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ApplicationResponse> deleteBook(@PathVariable Long id) {
        log.info("Received request to delete book with ID: {}", id);
        ApplicationResponse response = bookService.deleteBook(id);
        log.info("Deletion response: {}", response);

        return ResponseEntity.ok(response);
    }
}
