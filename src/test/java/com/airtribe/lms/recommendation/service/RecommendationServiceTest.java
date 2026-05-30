package com.airtribe.lms.recommendation.service;

import com.airtribe.lms.model.Book;
import com.airtribe.lms.model.Patron;
import com.airtribe.lms.repository.BookRepository;
import com.airtribe.lms.repository.PatronRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class RecommendationServiceTest {

    private RecommendationService recommendationService;

    @BeforeEach
    public void setUp() {
        recommendationService = new RecommendationService();
        
        // Ensure static data maps are initialized to prevent NullPointerException
        if (PatronRepository.patronData == null) {
            PatronRepository.patronData = new HashMap<>();
        }
        if (BookRepository.bookData == null) {
            BookRepository.bookData = new HashMap<>();
        }
        
        // Clear any leftover static data from previous test runs
        PatronRepository.patronData.clear();
        BookRepository.bookData.clear();
    }

    @AfterEach
    public void tearDown() {
        // Clean up static data so it doesn't affect other tests in your suite
        PatronRepository.patronData.clear();
        BookRepository.bookData.clear();
    }

    @Test
    public void testProvideRecommendationsByGenre_Success() {
        // Arrange
        String patronId = "P101";
        
        // Create a fake Patron and set their recommended genres
        Patron mockPatron = new Patron();
        Set<String> genres = new HashSet<>();
        genres.add("Sci-Fi");
        genres.add("Fantasy");
        // Assumes your Patron class has a setter for recommended genres or a way to mutate it
        // If your Patron class uses a different method name, adjust this line:
        mockPatron.setRecommendedGenres(genres); 
        
        // Inject fake patron into the static map
        PatronRepository.patronData.put(patronId, mockPatron);

        // Create a few mock books to populate the book library map
        Book book1 = new Book();
        book1.setGenre("sci-fi"); // Lowercase to test the service's case-insensitivity logic
        
        Book book2 = new Book();
        book2.setGenre("History"); // Genre that should be ignored
        
        Book book3 = new Book();
        book3.setGenre(null); // Explicitly testing the null check safeguard in your filter

        BookRepository.bookData.put("B1", book1);
        BookRepository.bookData.put("B2", book2);
        BookRepository.bookData.put("B3", book3);

        // Act & Assert
        // Since your method returns 'void' and prints straight to System.out, 
        // we verify that the execution processes all stream filtering cleanly without crashing.
        assertDoesNotThrow(() -> {
            recommendationService.provideRecommendationsByGenre(patronId);
        });
    }

    @Test
    public void testProvideRecommendationsByGenre_EmptyGenres() {
        // Arrange
        String patronId = "P102";
        Patron mockPatron = new Patron();
        mockPatron.setRecommendedGenres(Collections.emptySet());
        
        PatronRepository.patronData.put(patronId, mockPatron);

        // Act & Assert
        assertDoesNotThrow(() -> {
            recommendationService.provideRecommendationsByGenre(patronId);
        });
    }
}