package com.airtribe.lms.request.dto;

import java.time.LocalDate;

public class LenderRequest {
	private String isbn;
	private String patronId;
	private LocalDate returnDate;

	public LenderRequest(String isbn, String patronId) {
		this.isbn = isbn;
		this.patronId = patronId;
	}

	public LenderRequest(String isbn, String patronId, LocalDate returnDate) {
		this.isbn = isbn;
		this.patronId = patronId;
		this.returnDate = returnDate;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getPatronId() {
		return patronId;
	}

	public void setPatronId(String patronId) {
		this.patronId = patronId;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}

	@Override
	public String toString() {
		return "LenderRequest [isbn=" + isbn + ", patronId=" + patronId + ", returnDate=" + returnDate + "]";
	}

}
