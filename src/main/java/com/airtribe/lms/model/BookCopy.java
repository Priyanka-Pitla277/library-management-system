package com.airtribe.lms.model;

public class BookCopy {
	private String copyId;
	private String isbn;
	private String homeBranchId;
	private String currentBranchId;
	private String status;

	// 1. Private constructor ensures instances are only created via the Builder
	private BookCopy(Builder builder) {
		this.copyId = builder.copyId;
		this.isbn = builder.isbn;
		this.homeBranchId = builder.homeBranchId;
		this.currentBranchId = builder.currentBranchId;
		this.status = builder.status != null ? builder.status : "AVAILABLE"; // Sensible default
	}

	// --- Getters and Setters ---
	public String getCopyId() {
		return copyId;
	}

	public void setCopyId(String copyId) {
		this.copyId = copyId;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getHomeBranchId() {
		return homeBranchId;
	}

	public void setHomeBranchId(String homeBranchId) {
		this.homeBranchId = homeBranchId;
	}

	public String getCurrentBranchId() {
		return currentBranchId;
	}

	public void setCurrentBranchId(String currentBranchId) {
		this.currentBranchId = currentBranchId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "BookCopy [copyId=" + copyId + ", isbn=" + isbn + ", homeBranchId=" + homeBranchId + ", currentBranchId="
				+ currentBranchId + ", status=" + status + "]";
	}

	// --- 2. Static Nested Builder Class ---
	public static class Builder {
		private String copyId;
		private String isbn;
		private String homeBranchId;
		private String currentBranchId;
		private String status;

		public Builder() {
			// Empty constructor for fluent chaining
		}

		public Builder copyId(String copyId) {
			this.copyId = copyId;
			return this;
		}

		public Builder isbn(String isbn) {
			this.isbn = isbn;
			return this;
		}

		public Builder homeBranchId(String homeBranchId) {
			this.homeBranchId = homeBranchId;
			return this;
		}

		public Builder currentBranchId(String currentBranchId) {
			this.currentBranchId = currentBranchId;
			return this;
		}

		public Builder status(String status) {
			this.status = status;
			return this;
		}

		// 3. The final build method that returns the BookCopy instance
		public BookCopy build() {
			return new BookCopy(this);
		}
	}
}