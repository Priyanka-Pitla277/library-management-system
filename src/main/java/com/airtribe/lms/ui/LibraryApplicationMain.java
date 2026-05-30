package com.airtribe.lms.ui;

import com.airtribe.lms.constants.MenuOptions;
import com.airtribe.lms.enums.AlertType;
import com.airtribe.lms.library.service.BookInventoryManagementService;
import com.airtribe.lms.library.service.LibraryManagementService;
import com.airtribe.lms.library.service.LibraryManagementServiceImpl;
import com.airtribe.lms.library.service.PatronManagementService;
import com.airtribe.lms.logistic.service.LogisticsService;
import com.airtribe.lms.model.Book;
import com.airtribe.lms.model.BookCopy;
import com.airtribe.lms.model.Branch;
import com.airtribe.lms.model.Patron;
import com.airtribe.lms.request.dto.LenderRequest;
import com.airtribe.lms.util.InputValidator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;


public class LibraryApplicationMain {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        BookInventoryManagementService bookManagementService = new BookInventoryManagementService();
        PatronManagementService patronManagementService = new PatronManagementService();    
        LibraryManagementService libraryService = new LibraryManagementServiceImpl();

        Scanner scanner = new Scanner(System.in);
        boolean keepRunning = true;

        while (keepRunning) {
            try {
                printMenu();
                System.out.print("Selection: ");
                
                // Read full line to prevent scanner buffer mismatch issues
                int choice = Integer.parseInt(scanner.nextLine().trim());

				switch (choice) {
				case 0:
					bookManagementService.getAllBooksList();
					break;
				case 1:
					Book book = constructBook(scanner);
					bookManagementService.addBook(book);
					break;
				case 2:
					handleUpdateBook(scanner, bookManagementService);
					break;
				case 3:
					System.out.print("Enter search item: ");
					String searchField = scanner.nextLine().trim();
					bookManagementService.searchBook(searchField);
					break;
				case 4:
					System.out.print("Enter isbn: ");
					String deleteIsbn = scanner.nextLine().trim();
					bookManagementService.removeBook(deleteIsbn);
					break;
				case 5:
					handleAddPatron(scanner, patronManagementService);
					break;
				case 6:
					Patron patron = constructPatron(scanner);
					patronManagementService.updatePatron(patron);
					break;
				case 7:
					System.out.print("Enter patronid to search item: ");
					String searchPatronId = scanner.nextLine().trim();
					patronManagementService.getPatron(searchPatronId);
					break;
				case 8:
					handleCheckoutBook(scanner, libraryService);
					break;
				case 9:
					handleReturnBook(scanner, libraryService);
					break;
				case 10:
					handleReserveBook(scanner, libraryService);
					break;
                 case 11:
                     LogisticsService logistics = new LogisticsService();
                     handleLogisticsTransfer(scanner, logistics);
                     break;
				case 12:
					keepRunning = false;
					System.out.println("Exiting the interactive menu window.");
					break;
				default:
					InputValidator.invalidInput();
					break;
				}
            } catch (NumberFormatException | InputMismatchException e) {
                System.out.println("\n[Error] Invalid choice format. Please enter a valid number.\n");
            } catch (IllegalArgumentException e) {
                System.out.println("\n[Error] Argument error: " + e.getMessage() + "\n");
            } catch (DateTimeParseException e) {
                System.out.println("\n[Error] Date format mismatch. Use dd/MM/yyyy layout.\n");
            } catch (Exception e) {
                System.out.println("\n[Error] System issue: " + e.getMessage());
            }
        }
        
        scanner.close();

        // Run logistics pipeline after exiting menu operations loop
    }

    private static void printMenu() {
        System.out.println("\nSelect the operation you want to perform on book: " + MenuOptions.MENU);
        System.out.println("Select the operation you want to perform on patron: " + MenuOptions.PATRON_MENU);
        System.out.println("Select the operation you want to perform on services: " + MenuOptions.SERVICE_MENU);
    }

    // ==========================================
    // DELEGATED EXTENSION OPERATIONS (Java 8)
    // ==========================================

    private static void handleUpdateBook(Scanner scanner, BookInventoryManagementService service) {
        System.out.print("Enter isbn: ");
        String isbn = scanner.nextLine().trim();
        System.out.print("Enter title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter publication Year: ");
        int publicationYear = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter author: ");
        String author = scanner.nextLine().trim();
        service.updateBook(isbn, title, author, publicationYear);
    }

    private static void handleAddPatron(Scanner scanner, PatronManagementService service) {
        System.out.print("Enter patronId: ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter mobile: ");
        String mobileNo = scanner.nextLine().trim();
        System.out.print("Enter alertType (SMS/EMAIL): ");
        String alertTypeStr = scanner.nextLine().trim().toUpperCase();

        Patron patron = new Patron.Builder()
                .patronId(id)
                .name(name)
                .email(email)
                .mobileNo(mobileNo)
                .chosenalertType(AlertType.valueOf(alertTypeStr))
                .build();
        service.addPatron(patron);
    }

    private static void handleCheckoutBook(Scanner scanner, LibraryManagementService service) {
        System.out.print("Enter patronId: ");
        String patronId = scanner.nextLine().trim();
        System.out.print("Enter isbn: ");
        String isbn = scanner.nextLine().trim();
        System.out.print("Enter return date in format (dd/MM/yyyy): ");
        String returnDate = scanner.nextLine().trim();
        service.checkoutBook(new LenderRequest(isbn, patronId, LocalDate.parse(returnDate, DATE_FORMATTER)));
    }

    private static void handleReturnBook(Scanner scanner, LibraryManagementService service) {
        System.out.print("Enter patronId: ");
        String patronId = scanner.nextLine().trim();
        System.out.print("Enter isbn: ");
        String isbn = scanner.nextLine().trim();
        service.returnBook(new LenderRequest(isbn, patronId));
    }

    private static void handleReserveBook(Scanner scanner, LibraryManagementService service) {
        System.out.print("Enter patronId: ");
        String patronId = scanner.nextLine().trim();
        System.out.print("Enter isbn: ");
        String isbn = scanner.nextLine().trim();
        service.reserveBook(new LenderRequest(isbn, patronId));
    }

    private static Patron constructPatron(Scanner scanner) {
        System.out.print("Enter patronId: ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter mobile: ");
        String mobileNo = scanner.nextLine().trim();
        System.out.print("Enter alertType (SMS/EMAIL): ");
        String alertType = scanner.nextLine().trim().toUpperCase();
        
        return new Patron.Builder()
                .patronId(id)
                .name(name)
                .email(email)
                .mobileNo(mobileNo)
                .chosenalertType(AlertType.valueOf(alertType))
                .build();
    }

    private static Book constructBook(Scanner scanner) {
        System.out.print("Enter isbn: ");
        String isbn = scanner.nextLine().trim();
        System.out.print("Enter title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine().trim();
        System.out.print("Enter publication Year: ");
        int publicationYear = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter author: ");
        String author = scanner.nextLine().trim();
        
        return new Book.Builder()
                .title(title)
                .author(author)
                .publicationYear(publicationYear)
                .isbn(isbn)
                .genre(genre)
                .build();
    }


    private static void handleLogisticsTransfer(Scanner scanner, LogisticsService logisticsService) {
        System.out.println("\n--- Initiate Inter-Branch Book Transit ---");

        System.out.print("Enter Book Copy ID (e.g., BC-001): ");
        String copyId = scanner.nextLine().trim();

        System.out.print("Enter Source Branch ID (e.g., B-DOWNTOWN): ");
        String sourceBranchId = scanner.nextLine().trim();

        System.out.print("Enter Target Destination Branch ID (e.g., B-NORTHSIDE): ");
        String targetBranchId = scanner.nextLine().trim();
        Branch branch1 = new Branch(sourceBranchId, sourceBranchId);
        Branch branch2 = new Branch(targetBranchId, sourceBranchId);
        BookCopy cleanCodeCopy = new BookCopy.Builder()
                .copyId(copyId)
                .isbn("978-0132350884")
                .homeBranchId(sourceBranchId)
                .currentBranchId(sourceBranchId)
                .status("AVAILABLE")
                .build();
        branch1.addCopy(cleanCodeCopy);
        logisticsService.registerBranch(branch1);
        logisticsService.registerBranch(branch2);

        // Step 1: Initiate Transit (Locks status to IN_TRANSIT)
        boolean transitStarted = logisticsService.initiateTransit(copyId, sourceBranchId);

        if (!transitStarted) {
            System.out.println("❌ Error: Could not initiate transit. Verify the Copy ID exists at the source branch and is AVAILABLE.");
            return;
        }
        System.out.println("🚚 Success: Book copy [" + copyId + "] has left [" + sourceBranchId + "] and is currently IN_TRANSIT.");

        // Step 2: Receive Transit (Moves the map reference and sets status back to AVAILABLE)
        boolean transitReceived = logisticsService.receiveTransit(copyId, sourceBranchId, targetBranchId);

        if (transitReceived) {
            System.out.println("✅ Success: Branch [" + targetBranchId + "] has safely processed the courier. Asset is now AVAILABLE.");
        } else {
            System.out.println("❌ Critical Error: Transit failed mid-route. Target branch record not found.");
        }
    }

}
