package com.airtribe.lms.library.service;

import com.airtribe.lms.exception.ResourceNotFoundException;
import com.airtribe.lms.model.Book;
import com.airtribe.lms.repository.BookRepository;
import com.airtribe.lms.util.InputValidator;

import java.util.List;

public class BookInventoryManagementService {

	private BookRepository repository = new BookRepository();

	public void addBook(Book book) {
		repository.addBook(book);
	}

	public void removeBook(String isbn) {
		try {
			repository.removeBook(isbn);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void updateBook(String isbn, String title, String author, Integer pubYear) {
		try {
			repository.updateBook(isbn, title, author, pubYear);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public List<Book> searchBook(String searchField) {
		try {
			List<Book> searchedBooks = repository.searchBook(searchField);
			System.out.println(searchedBooks);
		} catch (ResourceNotFoundException e) {
			InputValidator.emptyData(e.getMessage());
		}
		return null;
	}

	public List<Book> availableBooksToCheckout() {
		return repository.availableBooksToCheckout();

	}

	public List<Book> borrowedBooksList() {
		return repository.borrowedBooksList();

	}

	public List<Book> getAllBooksList() {
		return repository.getAllBooks();

	}
}
