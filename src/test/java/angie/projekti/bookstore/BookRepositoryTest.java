package angie.projekti.bookstore;

import angie.projekti.bookstore.model.Book;
import angie.projekti.bookstore.model.BookRepository;
import angie.projekti.bookstore.model.Category;
import angie.projekti.bookstore.model.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @Rollback(false)
    public void testCreateBook() {
        // Create a new book category and save it
        Category fiction = new Category("Fiction");
        categoryRepository.save(fiction);

        // Save a new book
        Book book = new Book("Brave New World", "Aldous Huxley", 1932, "9780060850524", 15.99, fiction);
        bookRepository.save(book);

        // Check if the book was saved correctly
        assertThat(book.getId()).isNotNull();
        Optional<Book> foundBook = bookRepository.findById(book.getId());
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getTitle()).isEqualTo("Brave New World");
    }

    @Test
    public void testFindByTitle() {
        // Find a book by title
        List<Book> books = bookRepository.findByTitle("1984");

        // Check if the correct book is found
        assertThat(books).isNotEmpty();
        assertThat(books.get(0).getTitle()).isEqualTo("1984");
        assertThat(books.get(0).getAuthor()).isEqualTo("George Orwell");
    }

    @Test
    public void testFindByAuthor() {
        // Find books by the same author
        List<Book> books = bookRepository.findByAuthor("Harper Lee");

        // The book by Harper Lee is retrieved
        assertThat(books).isNotEmpty();
        assertThat(books.get(0).getAuthor()).isEqualTo("Harper Lee");
        assertThat(books.get(0).getTitle()).isEqualTo("To Kill a Mockingbird");
    }

    @Test
    public void testDeleteBook() {
        // Create and save a new book
        Category classic = categoryRepository.findByName("Classic").get(0);
        Book book = new Book("Test Book", "Test Author", 2024, "1234567890123", 20.00, classic);
        bookRepository.save(book);

        // Delete the book
        Long bookId = book.getId();
        bookRepository.deleteById(bookId);

        // Make sure the book no longer exists
        Optional<Book> deletedBook = bookRepository.findById(bookId);
        assertThat(deletedBook).isEmpty();
    }

    @Test
    public void testUpdateBook() {
        // Find a book, update the price
        List<Book> books = bookRepository.findByTitle("The Catcher in the Rye");
        Book book = books.get(0);
        double newPrice = 12.99;

        // Update the price and save
        book.setPrice(newPrice);
        bookRepository.save(book);

        // the price was updated correctly?
        Book updatedBook = bookRepository.findById(book.getId()).get();
        assertThat(updatedBook.getPrice()).isEqualTo(newPrice);
    }
}
