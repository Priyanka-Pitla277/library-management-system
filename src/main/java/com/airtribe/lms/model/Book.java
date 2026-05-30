package com.airtribe.lms.model;

import java.util.LinkedList;
import java.util.Queue;

public class Book {

	private String title;
	private String author;
	private int publicationYear;
	private String isbn;
	private boolean isAvailable;
	private boolean isReserved;
	private Queue<Patron> reservationQueue;
	private String genre;
	private String reservedPatronId;
//	public Book(String title, String author, int publicationYear, String isbn) {
//		this.title = title;
//		this.author = author;
//		this.publicationYear = publicationYear;
//		this.isbn = isbn;
//		this.isAvailable = true;
//	}

	public Book(Builder builder) {
		this.title = builder.title;
		this.author = builder.author;
		this.publicationYear = builder.publicationYear;
		this.isbn = builder.isbn;
		this.isAvailable = true;
		this.reservationQueue = new LinkedList<>();
		this.genre = builder.genre;
        this.reservedPatronId = " ";
	}

	public Book() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getPublicationYear() {
		return publicationYear;
	}

	public void setPublicationYear(int publicationYear) {
		this.publicationYear = publicationYear;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public boolean isReserved() {
		return isReserved;
	}

	public void setReserved(boolean isReserved) {
		this.isReserved = isReserved;
	}

	public Queue<Patron> getReservationQueue() {
		return reservationQueue;
	}

	public void setReservationQueue(Queue<Patron> reservationQueue) {
		this.reservationQueue = reservationQueue;
	}


	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getReservedPatronId() {
		return reservedPatronId;
	}

	public void setReservedPatronId(String reservedPatronId) {
		this.reservedPatronId = reservedPatronId;
	}

	@Override
	public String toString() {
		return "Book [title=" + title + ", author=" + author + ", publicationYear=" + publicationYear + ", isbn=" + isbn
				+ ", isAvailable=" + isAvailable + ", isReserved=" + isReserved +  ", genre=" + genre + ", reservedPatronId=" + reservedPatronId + "]";
	}






	public static class Builder {

		private String title;
		private String author;
		private int publicationYear;
		private String isbn;
		private String genre;
//		private boolean isAvailable;


		public Builder title(String title) {
			this.title = title;
			return this;
		}

		public Builder author(String author) {
			this.author = author;
			return this;

		}

		public Builder publicationYear(int publicationYear) {
			this.publicationYear = publicationYear;
			return this;

		}

		public Builder isbn(String isbn) {
			this.isbn = isbn;
			return this;

		}


		public Builder genre(String genre) {
			this.genre = genre;
			return this;
		}

		public Book build() {
			return new Book(this);
		}

	}

}
