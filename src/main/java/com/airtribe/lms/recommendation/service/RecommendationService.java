package com.airtribe.lms.recommendation.service;

import com.airtribe.lms.model.Book;
import com.airtribe.lms.model.Patron;
import com.airtribe.lms.repository.BookRepository;
import com.airtribe.lms.repository.PatronRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class RecommendationService {

	public void provideRecommendationsByGenre(String patronId) {
		Patron patron = PatronRepository.patronData.get(patronId);
		Set<Book> bookList = new HashSet<>(BookRepository.bookData.values());

		Set<String> recommended = patron.getRecommendedGenres();

		// Allocate exact bucket size up front to prevent HashSet internal rehashing
		Set<String> preferredGenres = recommended.stream().map(String::toLowerCase)
				.collect(Collectors.toCollection(() -> new HashSet<>(recommended.size())));

		Set<Book> recommendationList = bookList.stream()
				.filter(book -> book.getGenre() != null && preferredGenres.contains(book.getGenre().toLowerCase()))
				.collect(Collectors.toSet());
		System.out.println(recommendationList);
	}
}
