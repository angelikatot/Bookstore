package angie.projekti.bookstore;

import angie.projekti.bookstore.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class BookstoreApplication {

	private static final Logger log = LoggerFactory.getLogger(BookstoreApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}

	@Bean
	public CommandLineRunner bookstoreDemo(BookRepository bookRepository, CategoryRepository categoryRepository,
			AppUserRepository appUserRepository) {
		return (args) -> {
			log.info("Saving some categories");
			categoryRepository.save(new Category("Fiction"));
			categoryRepository.save(new Category("Classic"));
			categoryRepository.save(new Category("Non-fiction"));

			log.info("Saving some books");
			saveBookWithCategory(bookRepository, categoryRepository, "1984", "George Orwell", 1949, "9780451524935",
					9.99, "Fiction");
			saveBookWithCategory(bookRepository, categoryRepository, "To Kill a Mockingbird", "Harper Lee", 1960,
					"9780060935467", 14.99, "Classic");
			saveBookWithCategory(bookRepository, categoryRepository, "The Catcher in the Rye", "J.D. Salinger", 1951,
					"9780316769488", 10.99, "Classic");

			log.info("Fetching all books");
			for (Book book : bookRepository.findAll()) {
				log.info(book.toString());
			}

			// Create users: user/user admin/admin
			AppUser user1 = new AppUser("user", "$2a$06$3jYRJrg0ghaaypjZ/.g4SethoeA51ph3UD4kZi9oPkeMTpjKU5uo6", "USER");
			AppUser user2 = new AppUser("admin", "$2a$10$0MMwY.IQqpsVc1jC8u7IJ.2rT8b0Cd3b3sfIBGV2zfgnPGtT4r0.C",
					"ADMIN");
			appUserRepository.save(user1);
			appUserRepository.save(user2);

			log.info("Created users: user (USER role) and admin (ADMIN role)");
		};
	}

	private void saveBookWithCategory(BookRepository bookRepository, CategoryRepository categoryRepository,
			String title, String author, int year, String isbn, double price, String categoryName) {
		List<Category> categories = categoryRepository.findByName(categoryName);
		if (!categories.isEmpty()) {
			Book book = new Book(title, author, year, isbn, price, categories.get(0));
			bookRepository.save(book);
		} else {
			log.warn("Category not found: " + categoryName);
		}
	}
}