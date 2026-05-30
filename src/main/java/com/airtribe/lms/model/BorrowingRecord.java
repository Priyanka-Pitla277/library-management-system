package com.airtribe.lms.model;

import java.time.LocalDate;

public class BorrowingRecord {

	private Book book;
	private LocalDate checoutDate;
	private LocalDate returnDate;

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public LocalDate getChecoutDate() {
		return checoutDate;
	}

	public void setChecoutDate(LocalDate checoutDate) {
		this.checoutDate = checoutDate;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}

	@Override
	public String toString() {
		return "BorrowingRecord [book=" + book + ", checoutDate=" + checoutDate + ", returnDate=" + returnDate + "]";
	}

}
