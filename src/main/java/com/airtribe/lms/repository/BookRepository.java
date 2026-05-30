package com.airtribe.lms.repository;

import com.airtribe.lms.exception.ResourceNotFoundException;
import com.airtribe.lms.model.Book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BookRepository {
	public static Map<String, Book> bookData = new HashMap<>();
	
	public void addBook(Book book) {
		if (bookData.containsKey(book.getIsbn())) {
			System.out.println("book already exists");
		} else {
			bookData.put(book.getIsbn(), book);
			System.out.println("book added successfully");
		}
	}
	
	public Book getBook(String isbn) {
		if (bookData.containsKey(isbn)) {
			return bookData.get(isbn);
		} else {
			throw new ResourceNotFoundException("No book available");
		}
	}

	public void removeBook(String isbn) {
		if (bookData.containsKey(isbn)) {
			bookData.remove(isbn);
			System.out.println("book removed successfully");
		} else {
			throw new ResourceNotFoundException("No book available to remove");
		}
	}
	
	public void updateBook(String isbn, String title, String author, Integer pubYear) {
		if (bookData.containsKey(isbn)) {
			Book book = bookData.get(isbn);
            boolean anyChanges = false;
			if (title != null) {
				book.setTitle(title);
				anyChanges = true;
			}
			if (author != null) {
				book.setAuthor(author);
				anyChanges = true;
			}
			if (title != null) {
				book.setTitle(title);
				anyChanges = true;
			}
			if (pubYear != null) {
				book.setPublicationYear(pubYear);
				anyChanges = true;
			}
			if (anyChanges) {
				System.out.println("updated successfully");
			}
			else if (!anyChanges) {
				System.out.println("No changes in the details you have entered to update");

			}
		} else {
			throw new ResourceNotFoundException("No Book available");
		}
	}
	
	public List<Book> searchBook(String searchField) {
		List<Book> booksAvailable = new ArrayList<>();
		for (Map.Entry<String, Book> entry : bookData.entrySet()) {
			Book searchBook = entry.getValue();
			if (searchBook.getAuthor().toLowerCase().contains(searchField.toLowerCase())
					|| searchBook.getTitle().toLowerCase().contains(searchField.toLowerCase())
					|| searchBook.getIsbn().toLowerCase().contains(searchField.toLowerCase())) {
				booksAvailable.add(entry.getValue());
			}
		}
		if (booksAvailable.isEmpty())
			throw new ResourceNotFoundException("no books available");
		return booksAvailable;
	}
	
	
	public List<Book> availableBooksToCheckout() {
		List<Book> availableBooks = bookData.values().stream().filter(book -> book.isAvailable())
				.collect(Collectors.toList());
		System.out.println("Number of books available: " + availableBooks.size());
		System.out.println(availableBooks);
		return availableBooks;
	}
	
	public List<Book> borrowedBooksList() {
		List<Book> borrowedBooks = bookData.values().stream().filter(book -> !book.isAvailable())
				.collect(Collectors.toList());
		System.out.println("Number of borrowed books: "+borrowedBooks.size());
		System.out.println(borrowedBooks);
		return borrowedBooks;

	}
	
	public List<Book> getAllBooks() {
		List<Book> bookList = new ArrayList<>(bookData.values());
		System.out.println(bookList);
		return bookList;

	}

}
