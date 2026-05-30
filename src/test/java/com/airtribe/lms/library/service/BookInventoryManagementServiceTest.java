package com.airtribe.lms.library.service;

import com.airtribe.lms.exception.ResourceNotFoundException;
import com.airtribe.lms.model.Book;
import com.airtribe.lms.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookInventoryManagementServiceTest {

    private BookInventoryManagementService inventoryService;

    @Mock
    private BookRepository mockRepository;

    private Book testBook;
    private final String sampleIsbn = "978-0132350884";

    @BeforeEach
    void setUp() throws Exception {
        inventoryService = new BookInventoryManagementService();

        // Use Java Reflection to inject our mocked repository into the private field
        // to cleanly bypass the inline instantiation.
        Field repoField = BookInventoryManagementService.class.getDeclaredField("repository");
        repoField.setAccessible(true);
        repoField.set(inventoryService, mockRepository);

        // Initialize a standard Book data model fixture for consistent usage across tests
        testBook = new Book.Builder()
                .isbn(sampleIsbn)
                .title("Clean Code")
                .author("Robert C. Martin")
                .publicationYear(2008)
                .build();
    }

    @Test
    @DisplayName("Should successfully delegate adding a book to the repository layer")
    void testAddBook_Success() {
        // Action
        inventoryService.addBook(testBook);

        // Assert/Verify
        verify(mockRepository, times(1)).addBook(testBook);
    }

    @Test
    @DisplayName("Should invoke repository remove when valid ISBN is provided")
    void testRemoveBook_Success() {
        // Action
        inventoryService.removeBook(sampleIsbn);

        // Assert/Verify
        verify(mockRepository, times(1)).removeBook(sampleIsbn);
    }

    @Test
    @DisplayName("Should gracefully handle exceptions during book removal without throwing them to client")
    void testRemoveBook_ExceptionHandling() {
        // Arrange
        doThrow(new RuntimeException("Database connection failure")).when(mockRepository).removeBook(sampleIsbn);

        // Action & Assert
        // We assert doesNotThrow because your method wraps operations inside a try-catch block
        assertDoesNotThrow(() -> inventoryService.removeBook(sampleIsbn));
        verify(mockRepository, times(1)).removeBook(sampleIsbn);
    }

    @Test
    @DisplayName("Should delegate book content update values correctly to repository layer")
    void testUpdateBook_Success() {
        // Action
        inventoryService.updateBook(sampleIsbn, "New Title", "New Author", 2024);

        // Assert/Verify
        verify(mockRepository, times(1)).updateBook(sampleIsbn, "New Title", "New Author", 2024);
    }

    @Test
    @DisplayName("Should handle exceptions gracefully during book detail updates")
    void testUpdateBook_ExceptionHandling() {
        // Arrange
        doThrow(new RuntimeException("Target ISBN missing")).when(mockRepository)
                .updateBook(anyString(), anyString(), anyString(), anyInt());

        // Action & Assert
        assertDoesNotThrow(() -> inventoryService.updateBook(sampleIsbn, "Title", "Author", 2020));
        verify(mockRepository, times(1)).updateBook(sampleIsbn, "Title", "Author", 2020);
    }

    @Test
    @DisplayName("Should look up books via repository but return null as per current design spec")
    void testSearchBook_Success() {
        // Arrange
        String query = "Clean";
        List<Book> expectedList = Collections.singletonList(testBook);
        when(mockRepository.searchBook(query)).thenReturn(expectedList);

        // Action
        List<Book> result = inventoryService.searchBook(query);

        // Assert
        // NOTE: Your current implementation prints search details to System.out but returns null explicitly.
        assertNull(result, "Service method currently hardcodes a null return value");
        verify(mockRepository, times(1)).searchBook(query);
    }

    @Test
    @DisplayName("Should catch ResourceNotFoundException during lookup and route to InputValidator handler")
    void testSearchBook_ResourceNotFoundException() {
        // Arrange
        String query = "Unknown Book";
        when(mockRepository.searchBook(query)).thenThrow(new ResourceNotFoundException("No books found matching query"));

        // Action & Assert
        // Caught by the internal catch block and routed safely to InputValidator handler
        assertDoesNotThrow(() -> inventoryService.searchBook(query));
        verify(mockRepository, times(1)).searchBook(query);
    }

    @Test
    @DisplayName("Should return list of books available to checkout from repository layer")
    void testAvailableBooksToCheckout() {
        // Arrange
        List<Book> expectedList = Arrays.asList(testBook);
        when(mockRepository.availableBooksToCheckout()).thenReturn(expectedList);

        // Action
        List<Book> actualList = inventoryService.availableBooksToCheckout();

        // Assert
        assertNotNull(actualList);
        assertEquals(1, actualList.size());
        assertEquals("Clean Code", actualList.get(0).getTitle());
        verify(mockRepository, times(1)).availableBooksToCheckout();
    }

    @Test
    @DisplayName("Should pull valid list of checked out items from repository layer")
    void testBorrowedBooksList() {
        // Arrange
        List<Book> expectedList = Arrays.asList(testBook);
        when(mockRepository.borrowedBooksList()).thenReturn(expectedList);

        // Action
        List<Book> actualList = inventoryService.borrowedBooksList();

        // Assert
        assertNotNull(actualList);
        assertEquals(1, actualList.size());
        verify(mockRepository, times(1)).borrowedBooksList();
    }

    @Test
    @DisplayName("Should retrieve full master inventory catalog collection safely from repository layer")
    void testGetAllBooksList() {
        // Arrange
        List<Book> expectedList = Arrays.asList(testBook);
        when(mockRepository.getAllBooks()).thenReturn(expectedList);

        // Action
        List<Book> actualList = inventoryService.getAllBooksList();

        // Assert
        assertNotNull(actualList);
        assertEquals(1, actualList.size());
        verify(mockRepository, times(1)).getAllBooks();
    }
}