package angie.projekti.bookstore.model;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {

    // Custom query method to find books by title
    List<Book> findByTitle(String title);

    // Custom query method to find books by author
    List<Book> findByAuthor(String author);
}
