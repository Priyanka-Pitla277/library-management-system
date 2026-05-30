package com.airtribe.lms.library.service;

import com.airtribe.lms.exception.ResourceNotFoundException;
import com.airtribe.lms.factory.NotificationSystemFactory;
import com.airtribe.lms.model.Book;
import com.airtribe.lms.model.BorrowingRecord;
import com.airtribe.lms.model.Patron;
import com.airtribe.lms.notifcation.service.NotificationSystem;
import com.airtribe.lms.repository.BookRepository;
import com.airtribe.lms.repository.PatronRepository;
import com.airtribe.lms.request.dto.LenderRequest;
import com.airtribe.lms.util.InputValidator;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class LibraryManagementServiceImpl implements LibraryManagementService {

	private BookRepository bookRepo = new BookRepository();
	private PatronRepository patronRepo = new PatronRepository();

	public void checkoutBook(LenderRequest request) {
		try {
			Book book = bookRepo.getBook(request.getIsbn());
			Patron patron = patronRepo.getPatron(request.getPatronId());
			if (!book.isAvailable() && !book.getReservedPatronId().equals(request.getPatronId())) {
				System.out.println("book is not available to checkout, you can reserve it for later");
				return;
			}
			BorrowingRecord record = new BorrowingRecord();
			if (null == request.getReturnDate()) {
				InputValidator.invalidFieldFound("return date cannot be null");
				return;
			}
			if (book.getReservationQueue().isEmpty()) {
				book.setReserved(false);
			}
			record.setReturnDate(request.getReturnDate());
			record.setChecoutDate(LocalDate.now());
			book.setAvailable(false);
			record.setBook(book);
			patron.getBorrowingHistory().add(record);
			patron.getRecommendedGenres().add(book.getGenre());
			System.out.println("book checked out");
			book.setReservedPatronId(" ");

		} catch (ResourceNotFoundException e) {
			InputValidator.emptyData(e.getMessage());
			return;
		}

	}

	public void returnBook(LenderRequest reuest) {
		List<BorrowingRecord> borrowRecords = null;
		Book returnedBook = null;
		try {
			Patron patron = patronRepo.getPatron(reuest.getPatronId());
			returnedBook = bookRepo.getBook(reuest.getIsbn());
			borrowRecords = patron.getBorrowingHistory();
			if (borrowRecords.isEmpty()) {
				System.out.println("no borrowed books");
				return;
			}
		} catch (ResourceNotFoundException e) {
			InputValidator.emptyData(e.getMessage());
			return;
		}
		BorrowingRecord record = null;

		for (BorrowingRecord rec : borrowRecords) {
			if (reuest.getIsbn().equals(rec.getBook().getIsbn())) {
				record = rec;
				break;
			}
		}
		borrowRecords.remove(record);
		if (!returnedBook.getReservationQueue().isEmpty()) {
			Patron patron = returnedBook.getReservationQueue().poll();
			System.out.println(patron.getName() + " your reserved book is ready you can checkit out");
			// notify user whenever book is available
			NotificationSystem notifcationService = NotificationSystemFactory
					.createNotificationEngine(patron.getChosenalertType());
			notifcationService.notifyUser(patron, returnedBook);
			returnedBook.setReservedPatronId(patron.getPatronId());
		}
		else {
			returnedBook.setReserved(false);
			returnedBook.setAvailable(true);
		}
		System.out.println("book returned");
//		returnedBook.setAvailable(true);

	}

	public void reserveBook(LenderRequest request) {
		Book book = bookRepo.getBook(request.getIsbn());
		Patron patron = patronRepo.getPatron(request.getPatronId());
		Optional<BorrowingRecord> foundBook = patron.getBorrowingHistory().stream()
				.filter(record -> request.getIsbn().equals(record.getBook().getIsbn())).findFirst(); 

		if (foundBook.isPresent()) {
			System.out.println("already you checkout same book, please try again with different isbn");
			return;
		}
		if (null != book) {
			if (book.isAvailable()) {
				System.out.println("book is available, you cannot reserve proceed for direct checkout");
				return;
			} else if (book.getReservationQueue().contains(patron)) {
				System.out.println(patron.getName() + " is already in reservation queue"); 
				return;
			}
			patron.getRecommendedGenres().add(book.getGenre());
			book.setReserved(true);
			book.getReservationQueue().add(patron);
			System.out.println("Success: " + patron.getName() + " placed a hold on '" + book.getTitle() + "'.");
		}
	}

}
