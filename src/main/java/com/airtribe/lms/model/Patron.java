package com.airtribe.lms.model;

import com.airtribe.lms.enums.AlertType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Patron {

	private String patronId;
	private String name;
	private String email;
	private String mobileNo;
	private List<BorrowingRecord> borrowingHistory;
	private AlertType chosenalertType;
	private Set<String> recommendedGenres;

	// 1. Private constructor ensures Patrons can only be made via the Builder
	private Patron(Builder builder) {
		this.patronId = builder.patronId;
		this.name = builder.name;
		this.email = builder.email;
		this.mobileNo = builder.mobileNo;
		this.borrowingHistory = builder.borrowingHistory != null ? builder.borrowingHistory : new ArrayList<>();
		this.chosenalertType = builder.chosenalertType;
		this.recommendedGenres = builder.recommendedGenres != null ? builder.recommendedGenres : new HashSet<>();
		System.out.println(this);
	}

	public Patron() {
		super();
	}

	// --- Getters and Setters ---
	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getPatronId() {
		return patronId;
	}

	public void setPatronId(String patronId) {
		this.patronId = patronId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<BorrowingRecord> getBorrowingHistory() {
		return borrowingHistory;
	}

	public void setBorrowingHistory(List<BorrowingRecord> borrowingHistory) {
		this.borrowingHistory = borrowingHistory;
	}

	public AlertType getChosenalertType() {
		return chosenalertType;
	}

	public void setChosenalertType(AlertType chosenalertType) {
		this.chosenalertType = chosenalertType;
	}

	public Set<String> getRecommendedGenres() {
		return recommendedGenres;
	}

	public void setRecommendedGenres(Set<String> recommendedGenres) {
		this.recommendedGenres = recommendedGenres;
	}

	@Override
	public String toString() {
		return "Patron [patronId=" + patronId + ", name=" + name + ", email=" + email + ", mobileNo=" + mobileNo
				+ ", chosenalertType=" + chosenalertType + "]";
	}

	// --- 2. Static Nested Builder Class ---
	public static class Builder {
		private String patronId;
		private String name;
		private String email;
		private String mobileNo;
		private List<BorrowingRecord> borrowingHistory = new ArrayList<>(); // Default Initialization
		private AlertType chosenalertType;
		private Set<String> recommendedGenres = new HashSet<>(); // Default Initialization

		public Builder() {
			// Empty constructor for fluent building
		}

		public Builder patronId(String patronId) {
			this.patronId = patronId;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder email(String email) {
			this.email = email;
			return this;
		}

		public Builder mobileNo(String mobileNo) {
			this.mobileNo = mobileNo;
			return this;
		}

		public Builder borrowingHistory(List<BorrowingRecord> borrowingHistory) {
			this.borrowingHistory = borrowingHistory;
			return this;
		}

		public Builder chosenalertType(AlertType chosenalertType) {
			this.chosenalertType = chosenalertType;
			return this;
		}

		public Builder recommendedGenres(Set<String> recommendedGenres) {
			this.recommendedGenres = recommendedGenres;
			return this;
		}

		// 3. The final build step that returns the Patron instance
		public Patron build() {
			return new Patron(this);
		}
	}

}