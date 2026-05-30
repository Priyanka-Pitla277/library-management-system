package com.airtribe.lms.repository;

import com.airtribe.lms.exception.ResourceNotFoundException;
import com.airtribe.lms.model.Patron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatronRepository {
	
	public static Map<String, Patron> patronData = new HashMap<>();

	public void addPatron(Patron patron) {
		if (patronData.containsKey(patron.getPatronId())) {
			System.out.println("patron already exists");
		} else {
			patronData.put(patron.getPatronId(), patron);
		}
	}
	
	public void removePatron(Patron patron) {
		if (!patronData.containsKey(patron.getPatronId())) {
			throw new ResourceNotFoundException("No patron found");
		} else {
			patronData.remove(patron.getPatronId());
		}
	}
	
	public void updatePatron(Patron patron) {
		if (patronData.containsKey(patron.getPatronId())) {
			Patron currentPatronData = patronData.get(patron.getPatronId());
			if (patron.getEmail() != null) {
				currentPatronData.setEmail(patron.getEmail());
			}
			if (patron.getName() != null) {
				currentPatronData.setName(patron.getName());
			}
			if (patron.getChosenalertType() != null) {
				currentPatronData.setChosenalertType(patron.getChosenalertType());
			}
			if (patron.getMobileNo() != null) {
				currentPatronData.setMobileNo(patron.getMobileNo());
			}
			System.out.println("patron details updated");
		} else {
			throw new ResourceNotFoundException("No patron found");
		}
	}
	
	
	public List<Patron> getPatronList() {
		List<Patron> patrons = new ArrayList<>(patronData.values());
		return patrons;

	}
	
	public Patron getPatron(String patronId) {
		Patron patron = null;
		if (patronData.containsKey(patronId)) {
			patron = patronData.get(patronId);		
			System.out.println(patron);
			System.out.println(patron.getBorrowingHistory());

		} else {
			throw new ResourceNotFoundException("No patron found");
		}
		return patron;
	}

}
