package angie.projekti.bookstore.web;

import angie.projekti.bookstore.model.Book;
import angie.projekti.bookstore.model.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        return "bookList";
    }

    @GetMapping("/new")
    public String addBook(Model model) {
        log.info("Creating a new book");
        model.addAttribute("book", new Book());
        return "addbook";
    }

    @PostMapping("/save")
    public String saveBook(@ModelAttribute("book") Book book) {
        log.info("Saving book: " + book);
        bookRepository.save(book);
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable("id") Long id, Model model) {
        log.info("Editing book with ID: " + id);
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            model.addAttribute("book", book);
            return "editBook";
        } else {
            log.warn("Book with ID: " + id + " not found");
            return "redirect:/books";
        }
    }

    @PostMapping("/update")
    public String updateBook(@ModelAttribute("book") Book book) {
        log.info("Updating book with ID: " + book.getId());
        bookRepository.save(book);
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        log.info("Deleting book with ID: " + id);
        bookRepository.deleteById(id);
        return "redirect:/books";
    }
}
