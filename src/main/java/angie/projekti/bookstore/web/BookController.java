package angie.projekti.bookstore.web;

import angie.projekti.bookstore.model.Book;
import angie.projekti.bookstore.model.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

@Controller
public class BookController {

    private static final Logger log = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/booklist";
    }

    @GetMapping("/booklist")
    public String showBooks(Model model) {
        log.info("Fetching all books");
        model.addAttribute("books", bookRepository.findAll());
        return "bookList";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addBook(Model model) {
        log.info("Creating a new book");
        model.addAttribute("book", new Book());
        return "addbook";
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String saveBook(@ModelAttribute("book") Book book) {
        log.info("Saving book: " + book);
        bookRepository.save(book);
        return "redirect:/booklist";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String editBook(@PathVariable("id") Long id, Model model) {
        log.info("Editing book with ID: " + id);
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            model.addAttribute("book", book);
            return "editBook";
        } else {
            log.warn("Book with ID: " + id + " not found");
            return "redirect:/booklist";
        }
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String updateBook(@ModelAttribute("book") Book book) {
        log.info("Updating book with ID: " + book.getId());
        bookRepository.save(book);
        return "redirect:/booklist";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteBook(@PathVariable("id") Long id) {
        log.info("Deleting book with ID: " + id);
        bookRepository.deleteById(id);
        return "redirect:/booklist";
    }

    // RESTful service to get all books
    @GetMapping("/books")
    public @ResponseBody List<Book> bookListRest() {
        return (List<Book>) bookRepository.findAll();
    }

    // RESTful service to get book by id
    @GetMapping("/book/{id}")
    public @ResponseBody Optional<Book> findBookRest(@PathVariable("id") Long bookId) {
        return bookRepository.findById(bookId);
    }
}
