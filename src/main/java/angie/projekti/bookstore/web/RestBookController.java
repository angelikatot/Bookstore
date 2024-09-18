package angie.projekti.bookstore.web;

import angie.projekti.bookstore.model.Book;
import angie.projekti.bookstore.model.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@RestController
@RequestMapping("/api/books")
public class RestBookController {

    private static final Logger log = LoggerFactory.getLogger(RestBookController.class);
    private final BookRepository bookRepository;

    public RestBookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // 1. Get all books as JSON
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "categories" })
    @GetMapping("/all")
    public List<Book> getAllBooks() {
        log.info("Fetching all books as JSON");
        return (List<Book>) bookRepository.findAll();
    }

    // 2. Get one book by ID as JSON
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable("id") Long id) {
        log.info("Fetching book with ID: " + id);
        return bookRepository.findById(id).orElse(null);
    }

    // Create or update a book
    @PostMapping
    public Book createOrUpdateBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    // Delete book by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // HTTP 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // HTTP 404 Not Found
        }
    }
}
