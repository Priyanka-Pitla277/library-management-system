package com.airtribe.lms.library.service;

import com.airtribe.lms.exception.ResourceNotFoundException;
import com.airtribe.lms.model.Patron;
import com.airtribe.lms.repository.PatronRepository;
import com.airtribe.lms.util.InputValidator;

import java.util.List;

public class PatronManagementService {
	private PatronRepository repository = new PatronRepository();

	public void addPatron(Patron patron) {
		repository.addPatron(patron);
	}

	public void removePatron(Patron patron) {
		try {
			repository.removePatron(patron);
		} catch (ResourceNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	public void updatePatron(Patron patron) {
		try {
			repository.updatePatron(patron);
		} catch (ResourceNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public List<Patron> getAllPatrons() {
		return repository.getPatronList();
		
	}
	
	public void getPatron(String patronId) {
		try {
			repository.getPatron(patronId);
		} catch (ResourceNotFoundException e) {
			InputValidator.emptyData(e.getMessage());
		}

	}
}
