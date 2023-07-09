package dm.balaev.Bookbooking.persistance.repository;

import dm.balaev.Bookbooking.persistance.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthor(String author);
    Optional<Book> findByTitle(String name);
}
