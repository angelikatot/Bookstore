package angie.projekti.bookstore.web;

import angie.projekti.bookstore.model.Book;
import angie.projekti.bookstore.model.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/books")
public class BookController {

    private static final Logger log = LoggerFactory.getLogger(BookController.class);

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping({ "", "/index" })
    public String showBooks(Model model) {
        log.info("Fetching all books");
        model.addAttribute("books", bookRepository.findAll());
        return "bookList"; // Thymeleaf template name for displaying books
    }

    @GetMapping("/new")
    public String addBook(Model model) {
        log.info("Creating a new book");
        model.addAttribute("book", new Book());
        return "newBook"; // Thymeleaf template name for creating a new book
    }

    @PostMapping("/save")
    public String saveBook(@ModelAttribute("book") Book book) {
        log.info("Saving book: " + book);
        bookRepository.save(book);
        return "redirect:/books"; // Redirect to the list of books
    }

    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable("id") Long id, Model model) {
        log.info("Editing book with ID: " + id);
        model.addAttribute("book", bookRepository.findById(id).orElse(null));
        return "editBook"; // Thymeleaf template name for editing a book
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        log.info("Deleting book with ID: " + id);
        bookRepository.deleteById(id);
        return "redirect:/books"; // Redirect to the list of books
    }
}
