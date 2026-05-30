package com.airtribe.lms.library.service;

import com.airtribe.lms.model.Book;
import com.airtribe.lms.model.BorrowingRecord;
import com.airtribe.lms.model.Patron;
import com.airtribe.lms.repository.BookRepository;
import com.airtribe.lms.repository.PatronRepository;
import com.airtribe.lms.request.dto.LenderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LibraryManagementServiceImplTest {

    @Mock
    private BookRepository bookRepo;

    @Mock
    private PatronRepository patronRepo;

    @InjectMocks
    private LibraryManagementServiceImpl libraryService;

    private Book testBook;
    private Patron testPatron;

    @BeforeEach
    void setUp() {
        // Initialize sample Book domain model
        testBook = new Book();
        testBook.setIsbn("978-12345");
        testBook.setTitle("Clean Code");
        testBook.setGenre("Technology");
        testBook.setAvailable(true);
        testBook.setReservationQueue(new LinkedList<>());
        testBook.setReservedPatronId(" ");

        // Initialize sample Patron domain model
        testPatron = new Patron();
        testPatron.setPatronId("PATRON-01");
        testPatron.setName("Alice");
        testPatron.setBorrowingHistory(new ArrayList<>());
        testPatron.setRecommendedGenres(new HashSet<>());
    }

    // ==========================================
    // CHECKOUT BOOK TESTS
    // ==========================================

    @Test
    void testCheckoutBook_Success_WhenAvailable() {
        LenderRequest request = new LenderRequest("978-12345", "PATRON-01", LocalDate.now().plusDays(14));

        when(bookRepo.getBook("978-12345")).thenReturn(testBook);
        when(patronRepo.getPatron("PATRON-01")).thenReturn(testPatron);

        libraryService.checkoutBook(request);

        assertFalse(testBook.isAvailable(), "Book availability should change to false upon checkout.");
        assertEquals(1, testPatron.getBorrowingHistory().size(), "Patron tracking history must record checkout.");
        assertTrue(testPatron.getRecommendedGenres().contains("Technology"), "Genre should be appended.");
    }

    @Test
    void testCheckoutBook_Fails_WhenBookUnavailableAndNotReservedByPatron() {
        testBook.setAvailable(false);
        testBook.setReservedPatronId("SOME-OTHER-ID");
        LenderRequest request = new LenderRequest("978-12345", "PATRON-01", LocalDate.now().plusDays(14));

        when(bookRepo.getBook("978-12345")).thenReturn(testBook);
        when(patronRepo.getPatron("PATRON-01")).thenReturn(testPatron);

        libraryService.checkoutBook(request);

        // Verify checkout was blocked
        assertEquals(0, testPatron.getBorrowingHistory().size(), "Checkout history must remain empty.");
    }

    // ==========================================
    // RETURN BOOK TESTS
    // ==========================================

    @Test
    void testReturnBook_Success_NoPendingReservations() {
        LenderRequest request = new LenderRequest("978-12345", "PATRON-01");

        // Setup existing active checkout record state
        BorrowingRecord record = new BorrowingRecord();
        record.setBook(testBook);
        testPatron.getBorrowingHistory().add(record);
        testBook.setAvailable(false);

        when(patronRepo.getPatron("PATRON-01")).thenReturn(testPatron);
        when(bookRepo.getBook("978-12345")).thenReturn(testBook);

        libraryService.returnBook(request);

        assertTrue(testBook.isAvailable(), "Book state flags must cycle back to available.");
        assertTrue(testPatron.getBorrowingHistory().isEmpty(), "Active collection match must be purged.");
    }

    // ==========================================
    // RESERVE BOOK TESTS
    // ==========================================

    @Test
    void testReserveBook_Success_WhenUnavailable() {
        testBook.setAvailable(false); // Book must be unavailable to hold a reservation
        LenderRequest request = new LenderRequest("978-12345", "PATRON-01");

        when(bookRepo.getBook("978-12345")).thenReturn(testBook);
        when(patronRepo.getPatron("PATRON-01")).thenReturn(testPatron);

        libraryService.reserveBook(request);

        assertTrue(testBook.getReservationQueue().contains(testPatron), "Patron must enter tracking queue.");
    }

    @Test
    void testReserveBook_Fails_WhenPatronAlreadyHasBookCheckedOut() {
        LenderRequest request = new LenderRequest("978-12345", "PATRON-01");

        // Simulate that user already owns this specific book right now
        BorrowingRecord record = new BorrowingRecord();
        record.setBook(testBook);
        testPatron.getBorrowingHistory().add(record);

        when(bookRepo.getBook("978-12345")).thenReturn(testBook);
        when(patronRepo.getPatron("PATRON-01")).thenReturn(testPatron);

        libraryService.reserveBook(request);

        assertFalse(testBook.getReservationQueue().contains(testPatron), "Action should bypass queue execution.");
    }
}
