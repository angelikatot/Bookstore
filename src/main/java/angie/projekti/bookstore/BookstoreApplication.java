package angie.projekti.bookstore;

import angie.projekti.bookstore.model.Book;
import angie.projekti.bookstore.model.BookRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookstoreApplication {

	private static final Logger log = LoggerFactory.getLogger(BookstoreApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}

	@Bean
	public CommandLineRunner demoData(BookRepository bookRepository) {
		return (args) -> {
			log.info("Saving some books");
			bookRepository.save(new Book("1984", "George Orwell", 1949, "9780451524935", 9.99));
			bookRepository.save(new Book("To Kill a Mockingbird", "Harper Lee", 1960, "9780060935467", 14.99));
			bookRepository.save(new Book("The Catcher in the Rye", "J.D. Salinger", 1951, "9780316769488", 10.99));

			log.info("Listing all books");
			for (Book book : bookRepository.findAll()) {
				log.info(book.toString());
			}
		};
	}
}
