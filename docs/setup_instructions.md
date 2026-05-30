## 🔬 Program Excecution

### Step 1: Bootstrap & Object Initialization

When the application binary is loaded, execution initializes within `LibraryApplicationMain.main(String[] args)`. The runtime sets up the necessary services and state variables:

1.  **Service Layer Setups:**

    -   A `BookInventoryManagementService` is constructed to handle separate inventory tasks.

    -   A `PatronManagementService` container is instantiated to manage reader metadata.

    -   A transactional service, `LibraryManagementServiceImpl`, is initialized to coordinate workflows like checking out, returning, and reserving books.

2.  **I/O Framework:** A standard `Scanner` is mapped to the standard input pipeline (`System.in`) to intercept console text commands.

3.  **State Configuration:** A loop control flag (`keepRunning`) is declared and flipped to `true`.


### Step 2: The Interactive Processing Menu Loop

The execution enters a continuous `while (keepRunning)` block. Every complete cycle goes through the following sub-steps:

1.  **Interface Printing:** The layout templates inside `MenuOptions` print directly to the console.

2.  **Buffer Mismatch Safeguard:** The application prints `Selection:` and processes the response using `Integer.parseInt(scanner.nextLine().trim())`. Reading the entire line protects the scanner from trailing carriage return line breaks.

3.  **Switch Context Delegation:** The parsed option routes into a `switch(choice)` block. Depending on the numeric evaluation, specific workflows execute:


#### Path A: Adding a Book (Choice 1)

-   Control branches out to the helper function `constructBook(scanner)`.

-   The console captures values for `isbn`, `title`, `genre`, `publicationYear`, and `author`.

-   A `Book` entity is generated using its internal `Builder` class parameters.

-   The object cascades to `bookManagementService.addBook(book)`, which delegates to `BookRepository.addBook(book)`.

-   The repository scans the static lookup map `bookData`. If unique, the instance is written to memory.


#### Path B: Registering a Patron (Choice 5)

-   Control routes to `handleAddPatron(scanner, patronManagementService)`.

-   The engine accepts entries for `patronId`, `name`, `email`, `mobile`, and `alertType`.

-   A `Patron` model is built via its matching `Builder` chain.

-   The runtime delegates to `repository.addPatron(patron)`, placing the object inside the static database map `PatronRepository.patronData`.


#### Path C: Checking Out a Book (Choice 8)

-   Control maps directly to `handleCheckoutBook(scanner, libraryService)`.

-   The application processes entries for `patronId`, `isbn`, and an explicit string date matching the pattern `dd/MM/yyyy`.

-   A data-transfer object `LenderRequest` is compiled and passed to `libraryService.checkoutBook(request)`.

-   **Inside `LibraryManagementServiceImpl.checkoutBook`:**

    -   The engine fetches the corresponding objects via `bookRepo.getBook()` and `patronRepo.getPatron()`.

    -   If the copy availability parameter evaluates to `false` (checked out), execution terminates unless the caller holds the active top slot inside the book's internal `reservationQueue`.

    -   If clear, it creates a `BorrowingRecord`, copies over data timestamps, sets `book.setAvailable(false)`, appends the ledger to the user's active history array, and logs `"book checked out"` to the console.


#### Path D: Returning a Book (Choice 9)

-   Control hits `handleReturnBook(scanner, libraryService)`.

-   A fresh `LenderRequest` specifying the target `isbn` and `patronId` passes into `libraryService.returnBook(request)`.

-   **Inside `LibraryManagementServiceImpl.returnBook`:**

    -   It isolates the explicit records matching the returning `isbn` inside the collection `patron.getBorrowingHistory()` and removes it.

    -   It checks if the item's `reservationQueue` contains waiting records.

    -   **If patrons are waiting:** It pops the highest-priority member (`reservationQueue.poll()`), assigns the reservation hold to their ID, parses their `ChosenalertType`, and passes the data to `NotificationSystemFactory`. The factory returns the correct alert engine type (`SMSNotificationSystem` or `EmailNotificationSystem`), which prints a target notification message.

    -   **If no patrons are waiting:** The item parameters cleanly shift back to `isAvailable = true`.


### Step 3: Global Fault Management & Exceptions

Any unexpected processing anomalies inside the menu cycle are caught by an overarching multi-catch error handling container wrapping the loop:

-   **Format Failures:** Processing non-numeric input string arrays trips a `NumberFormatException` or `InputMismatchException`, outputting a text warning.

-   **Date Parsing Errors:** Invalid formatting logs a `DateTimeParseException`, prompting the user to correct the input structure to the `dd/MM/yyyy` format.

-   **Business Boundary Failures:** Invalid parameters catch custom items such as `ResourceNotFoundException` or `InvalidFieldException` and route them through `InputValidator` to safely print error messages without crashing the program.


### Step 4: Graceful Exit & Teardown

When choice code **`11`** is registered, the application cleanly signs off from input monitoring loops:

1.  The execution flag `keepRunning` shifts to `false`, causing the loop structure to gracefully break on the next check cycle.

2.  The operational command `scanner.close()` runs to release platform terminal listener bindings.

3.  Execution drops out of the loop block and triggers the logistics routine `runLogisticsSimulation()`.


### Step 5: Logistics Node Simulation Workflow

The final phase of execution operates as an automated state simulation completely decoupled from user inputs:

1.  **Network Initialization:** A standalone `LogisticsService` engine instantiates along with two branch entities: `B-DOWNTOWN` and `B-NORTHSIDE`. Both branches register inside a thread-safe `ConcurrentHashMap` container network.

2.  **Asset Allocation:** An individual `BookCopy` item (`BC-001`) is built with its baseline availability state marked as `"AVAILABLE"` and loaded into the downtown branch storage network map.

3.  **Transit Dispatch (`initiateTransit`):**

    -   The program calls `logistics.initiateTransit("BC-001", "B-DOWNTOWN")`.

    -   The logistics engine fetches the item copy, confirms its active status, and flips the state property to `"IN_TRANSIT"`. This protects the asset, ensuring local patrons cannot check it out while it is physically moving between branches.

4.  **Transit Delivery Receipt (`receiveTransit`):**

    -   The system invokes `logistics.receiveTransit("BC-001", "B-DOWNTOWN", "B-NORTHSIDE")`.

    -   The method executes `source.removeCopy(copyId)`, completely erasing the book from the origin branch's data maps.

    -   The asset tracking location parameter updates to `"B-NORTHSIDE"`, its status restores back to `"AVAILABLE"`, and it loads safely into the destination node's inventory group.

5.  **Program Termination:** Once the logistics workflow completes, the `main` method hits its closing bracket, terminating the process with status code `0`.