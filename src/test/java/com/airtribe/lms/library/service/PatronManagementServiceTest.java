package com.airtribe.lms.library.service;

import com.airtribe.lms.exception.ResourceNotFoundException;
import com.airtribe.lms.model.Patron;
import com.airtribe.lms.repository.PatronRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Activates Mockito for JUnit 5 (Java 8 compatible)
public class PatronManagementServiceTest {

    @Mock
    private PatronRepository repository; // Creates a mock instance of the repository

    @InjectMocks
    private PatronManagementService service; // Injects the mock repository into the service

    @Test
    public void testAddPatron() {
        // Arrange
        Patron patron = new Patron(); // Assuming constructor/setters are available

        // Act
        service.addPatron(patron);

        // Assert
        verify(repository, times(1)).addPatron(patron); // Verifies the method was called exactly once
    }

    @Test
    public void testRemovePatron_Success() throws ResourceNotFoundException {
        // Arrange
        Patron patron = new Patron();
        doNothing().when(repository).removePatron(patron);

        // Act
        service.removePatron(patron);

        // Assert
        verify(repository, times(1)).removePatron(patron);
    }

    @Test
    public void testRemovePatron_ExceptionHandled() throws ResourceNotFoundException {
        // Arrange
        Patron patron = new Patron();
        // Simulate the repository throwing an exception when removing a patron
        doThrow(new ResourceNotFoundException("Patron not found")).when(repository).removePatron(patron);

        // Act
        service.removePatron(patron); // Should catch the exception internally and print to console

        // Assert
        verify(repository, times(1)).removePatron(patron);
    }

    @Test
    public void testUpdatePatron_Success() throws ResourceNotFoundException {
        // Arrange
        Patron patron = new Patron();
        doNothing().when(repository).updatePatron(patron);

        // Act
        service.updatePatron(patron);

        // Assert
        verify(repository, times(1)).updatePatron(patron);
    }

    @Test
    public void testGetAllPatrons() {
        // Arrange
        Patron p1 = new Patron();
        Patron p2 = new Patron();
        List<Patron> mockList = Arrays.asList(p1, p2);
        
        when(repository.getPatronList()).thenReturn(mockList);

        // Act
        List<Patron> result = service.getAllPatrons();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).getPatronList();
    }

    @Test
    public void testGetPatron_Success() throws ResourceNotFoundException {
        // Arrange
        String patronId = "P123";
        Patron patron = new Patron();
        when(repository.getPatron(patronId)).thenReturn(patron);

        // Act
        service.getPatron(patronId);

        // Assert
        verify(repository, times(1)).getPatron(patronId);
    }

    @Test
    public void testGetPatron_ExceptionHandled() throws ResourceNotFoundException {
        // Arrange
        String patronId = "P999";
        when(repository.getPatron(patronId)).thenThrow(new ResourceNotFoundException("Not Found"));

        // Act
        service.getPatron(patronId); // Internally catches exception and triggers InputValidator

        // Assert
        verify(repository, times(1)).getPatron(patronId);
    }
}