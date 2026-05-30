package com.airtribe.lms.library.service;

import com.airtribe.lms.request.dto.LenderRequest;

public interface LibraryManagementService {
	
	public void checkoutBook(LenderRequest request);

	public void returnBook(LenderRequest request);

	public void reserveBook(LenderRequest request);
}
