import java.io.File;
import java.io.FileNotFoundException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;

/**
 * title: Library.java
 * abstract: This class creates a Library where books (group of Book objects), shelves (group of Shelf objects),
 * and readers (group of Reader objects) are used.  The library is initialized by a file that MUST be in the
 * correct format to work.  No blank lines should be present in this file.  Books can be added, but not removed.
 * Shelves can be created, but not removed.  Readers can be added and removed.  Books are stored in the shelves
 * that match their subject.  Readers can check out books that are on the shelves and return books.  Shelves
 * keep track of how many of what books are stored there.  There are methods to display books, shelves (with
 * or without books), and readers (with and without books).
 * name: Juli S
 * date: 11/13/2021
 *
 * THOUGHTS:
 * (1): There needs to be consistency on how due dates are set and when.  Books added should have a
 * DEFAULT_DATE and when they are checked out the date should be either set by the file during init() or
 * set to today + LOAN_PERIOD.  The Util.java file hard codes the dueDate to today.  This is inconsistent.
 * (2): There should also be consideration whether some public methods should be private, but then I don't
 * know how to do jUnit test on private methods directly.
 * (3): Instead of hardcoding error messages, they should be taken from Code.java.  Some new codes can be
 * added and some messages need to be updated.  I created the errorMessage() method for implementing this.
 */

public class Library {
    public static final int LENDING_LIMIT = 5;  // Maximum number of books a reader can check out at a time
    public static final int LOAN_PERIOD = 21;   // The number of days after check out a book is due.
    private static final LocalDate DEFAULT_DATE = LocalDate.of(1970,01,01);
                                                // Used for setting default dates

    private static int libraryCard = 0;         // The current maximum library card number

    private String name;                        // Name of the library
    private List<Reader> readers;               // A list of readers registered to the library
    private HashMap<String, Shelf> shelves;     // Shelf Subject (String) key with Shelf objects (Shelf)
    private HashMap<Book, Integer> books;       // Books (Book) key with the number of books as values.

    public Library(String name) {
        this.name = name;
        this.books = new HashMap<>();
        this.shelves = new HashMap<>();
        this.readers = new ArrayList<>();
    }

    /**
     * This method init() parses the file(Library00.csv) into the collection of objects (Book, Shelf, Reader).
     * REQUIREMENT: File must NOT have empty lines between parsing sections.  An empty file is caught.
     * @param filename a String representation of the file's name including .csv extension.  Must be located at
     *                 the same level as the src folder.
     * @return a Code which indicates if there were any errors during processing.  If none, Code.SUCCESS is returned.
     */
    public Code init(String filename) {
        Scanner scan = null;
        File file = new File(filename);
        String lineInput;
        Integer recordCount;
        Code returnedCode;  // A returned Code from method calls.

        try {
            scan = new Scanner(file);
        } catch(FileNotFoundException e) {
            System.out.println(Code.FILE_NOT_FOUND_ERROR.getMessage());
//            e.printStackTrace();
            return Code.FILE_NOT_FOUND_ERROR;
        }

        //<------------------ PARSE BOOKS SECTION
        try {
            lineInput = scan.nextLine().trim();
        } catch(NoSuchElementException e) {
            System.out.println("NOTE: File is empty.  No book, shelf, or reader to parse.");
            return Code.SHELF_COUNT_ERROR;
        }

        recordCount = convertInt(lineInput, Code.BOOK_COUNT_ERROR);

        if(recordCount < 0) {
            returnedCode = errorCode(recordCount);
            return returnedCode;
        }

        System.out.println("Parsing " + recordCount + " books from file.");
        returnedCode = initBooks(recordCount, scan);

        System.out.println("PARSE BOOKS STATUS: " + returnedCode);
        System.out.println();
        listBooks();
        System.out.println();

        //<------------------ PARSE SHELF SECTION
        try {
            lineInput = scan.nextLine().trim();
        } catch(NoSuchElementException e) {
            System.out.println("NOTE: No shelf or reader to parse in file.");
            return Code.SHELF_COUNT_ERROR;
        }

        recordCount = convertInt(lineInput, Code.SHELF_COUNT_ERROR);

        if(recordCount < 0) {
            returnedCode = errorCode(recordCount);
            return returnedCode;
        }

        System.out.println("Parsing " + recordCount + " shelves from file.");
        returnedCode = initShelves(recordCount, scan);

        System.out.println("PARSE SHELF STATUS: " + returnedCode);
        System.out.println();
        returnedCode = listShelves(true);
        System.out.println();

        //<------------------ PARSE READER SECTION
        try {
            lineInput = scan.nextLine().trim();
        } catch(NoSuchElementException e) {
            System.out.println("NOTE: No readers in file.");
            return Code.READER_COUNT_ERROR;
        }

        recordCount = convertInt(lineInput, Code.READER_COUNT_ERROR);

        if(recordCount < 0) {
            returnedCode = errorCode(recordCount);
            return returnedCode;
        }

        System.out.println("Parsing " + recordCount + " readers from file.");
        returnedCode = initReader(recordCount, scan);

        System.out.println("PARSE READER STATUS: " + returnedCode);
//        System.out.println();
//        int count = listReaders(true);
//        System.out.println("Number of readers: " + count);
        System.out.println();

        System.out.println("FINISHED PARSING FROM FILE.\n");
        return Code.SUCCESS;
    }

    /**
     * This method initBooks() parses bookCount number of lines from the scan and creates a new Book from each line.
     * WARNING: A bad pageCount or dueDate displays a warning and discards that book entry.
     * @param bookCount the number of lines to read from scan.  A line represents one book data entry.
     * @param scan the Scanner of the file being used for data input
     * @return a Code which indicates if there were any errors during processing.  If none, Code.SUCCESS is returned.
     */
    private Code initBooks(int bookCount, Scanner scan) {
        String newBookEntry;  // line scan for a book's data

        if(bookCount < 1) {
            System.out.println("Error in initBooks(): bookCount is less than 1.");
            return Code.LIBRARY_ERROR;
        }

        for(int i = 0; i < bookCount; i++) {
            newBookEntry = scan.nextLine();
            System.out.println("-> Parsing book: " + newBookEntry);
            List<String> bookFields = Arrays.asList(newBookEntry.split(","));

            // There must be 6 fields to create a valid book.
            if(bookFields.size() == 6) {
                Code returnedCode;
                String isbn = bookFields.get(Book.ISBN_).trim();

                String title = bookFields.get(Book.TITLE_).trim();

                String subject = bookFields.get(Book.SUBJECT_).trim();

                int pageCount = convertInt(bookFields.get(Book.PAGE_COUNT_).trim(), Code.PAGE_COUNT_ERROR);
                if(pageCount < 0) {
                    returnedCode = errorCode(pageCount); // Decided to continue parsing and just discards entry.
                    System.out.println("ERROR: Invalid page count entry.");
                    System.out.println("WARNING: Could not parse book entry due to error. BOOK ENTRY DISCARDED.");
                    continue;
                }

                String author = bookFields.get(Book.AUTHOR_).trim();

                LocalDate dueDate = convertDate(bookFields.get(Book.DUE_DATE_).trim(), Code.DATE_CONVERSION_ERROR);
                if(dueDate == null) {
                    System.out.println("ERROR: Invalid date conversion.");
                    System.out.println("WARNING: Could not parse book entry due to error. BOOK ENTRY DISCARDED.");
                    continue;
                }

                Book newBook = new Book(isbn, title, subject, pageCount, author, dueDate);
                returnedCode = addBook(newBook);

            } else {
                return Code.UNKNOWN_ERROR;
            }
        }
        return Code.SUCCESS;
    }

    /**
     * This method initShelves() parses shelfCount number of lines from the scan and creates a new Shelf
     * from each line.
     * WARNING: A bad shelfNumber results in that record being discarded.
     * @param shelfCount the number of lines to read from scan.  A line represents one shelf data entry.
     * @param scan the Scanner of the file being used for data input
     * @return a Code which indicates if there were any errors during processing.  If none, Code.SUCCESS is returned.
     */
    private Code initShelves(int shelfCount, Scanner scan) {
        String newShelfEntry;  // line scan for a shelf's data

        if(shelfCount < 1) {
            System.out.println("Error in initShelves(): shelfCount is less than 1.");
            return Code.SHELF_COUNT_ERROR;
        }

        for(int i = 0; i < shelfCount; i++) {
            newShelfEntry = scan.nextLine();
            System.out.println("-> Parsing shelf: " + newShelfEntry);
            List<String> shelfFields = Arrays.asList(newShelfEntry.split(","));

            // There must be 2 fields to create a valid shelf.
            if(shelfFields.size() == 2) {
                Code returnedCode;

                int shelfNumber = convertInt(shelfFields.get(Shelf.SHELF_NUMBER_).trim(),
                        Code.SHELF_NUMBER_PARSE_ERROR);
                if(shelfNumber < 0) {
                    returnedCode = errorCode(shelfNumber); // Decided to continue parsing and just discards entry.
                    System.out.println("WARNING: Could not parse shelf due to error. SHELF ENTRY DISCARDED.");
                    continue;
                }

                String subject = shelfFields.get(Shelf.SUBJECT_).trim();

                Shelf newShelf = new Shelf();
                newShelf.setShelfNumber(shelfNumber);
                newShelf.setSubject(subject);
                returnedCode = addShelf(newShelf);

                /*
                WARNING: This section of code causes duplicate book on shelf error so do NOT use.
                Books are added to shelf in the addShelf(Shelf) method.
                // If newShelf was added successfully, loop through all books and add books with matching subjects
                if(returnedCode == Code.SUCCESS) {
                    for(Map.Entry<Book,Integer> book : books.entrySet()) {
                        if(book.getKey().getSubject().equals(subject)) {
                            int bookValue = book.getValue();
                            for(int num = 0; num < bookValue; num++) {
                                System.out.println("Adding: " + num);
                                addBookToShelf(book.getKey(),newShelf);
//                                newShelf.addBook(book.getKey()); // Using addBookToShelf instead of addBook()
                            }
                        }
                    }
                }
                */
            }
        }

        // Only time number of shelves does not match input counter is when a bad entry was discarded.
        if(shelfCount == shelves.size())
        {
            return Code.SUCCESS;
        } else {
            System.out.println("WARNING: " + (shelfCount - shelves.size())
                    + " shelf entries were lost due to errors.");
            return Code.SHELF_NUMBER_PARSE_ERROR;
        }
    }

    /**
     * This method initReader() parses readerCount number of lines from the scan and creates a new reader from
     * each line.
     * WARNING: A bad pageCount or dueDate displays a warning and discards that book entry.
     * WARNING: cardNumber for a reader is being set by library card class variable and NOT the first entry
     * in the scan line.  The design for library card number in this program needs to be better thought out.
     * @param readerCount the number of readers to scan.  A line represents one reader data entry.
     * @param scan the Scanner of the file being used for data input
     * @return a Code which indicates if there were any errors during processing.  If none, Code.SUCCESS is returned.
     */
    private Code initReader(int readerCount, Scanner scan) {
        String newReaderEntry;  // line scan for a readers's data

        if(readerCount < 1) {
            System.out.println("Error in initReader(): readerCount is less than 1.");
            return Code.READER_COUNT_ERROR;
        }

        for(int i = 0; i < readerCount; i++) {
            newReaderEntry = scan.nextLine();
//            System.out.println("-> Parsing reader: " + newReaderEntry);
            List<String> readerFields = Arrays.asList(newReaderEntry.split(","));

            // There must be at least 3 fields to create a valid reader.  4+ adds books to reader's list.
            if(readerFields.size() >= 3) {
                Code returnedCode;

                // cardNumber is being set by file scan.  Library card number is updated.
                int cardNumber = convertInt(readerFields.get(Reader.CARD_NUMBER_).trim(),
                        Code.READER_CARD_NUMBER_ERROR);
                if(cardNumber < 0) {
                    returnedCode = errorCode(cardNumber); // Decided to continue parsing and just discards entry.
                    System.out.println("WARNING: Could not parse reader due to error. READER ENTRY DISCARDED.");
                    continue;
                }

                // Library card number is updated to the largest of the numbers.
                if(cardNumber > libraryCard) {
                    libraryCard = cardNumber;
                }

                String name = readerFields.get(Reader.NAME_).trim();

                String phoneNumber = readerFields.get(Reader.PHONE_).trim();

                Reader newReader = new Reader(cardNumber, name, phoneNumber);
                returnedCode = addReader(newReader);

                // Reader needs books checked out if scan line has more than 3 entries.
                if(readerFields.size() > 3) {
                    int bookCount = convertInt(readerFields.get(Reader.BOOK_COUNT_).trim(), Code.BOOK_COUNT_ERROR);
                    if(bookCount < 0) {
                        returnedCode = errorCode(bookCount); // Decided to continue parsing and just discards entry.
                        System.out.println("ERROR: Invalid checked out book count entry.");
                        System.out.println("WARNING: Could not parse reader entry due to error.  "
                                + "READER ENTRY DISCARDED.");
                        continue;
                    }

                    // Make sure that the calculated numberOfCheckedOutBooks is the same as scanned bookCount.
                    int numberOfCheckedOutBooks = (readerFields.size() - Reader.BOOK_COUNT_) / 2;
                    if(bookCount != numberOfCheckedOutBooks) {
                        System.out.println("WARNING: bookCount does not match reader data.");
                    }

                    int fieldIndex = Reader.BOOK_START_;
                    String bookISBN;
                    LocalDate dueDate;
                    Book readersBook;

                    // Prevent going out-of-bounds
                    while(fieldIndex + 1 < readerFields.size()) {
                        // find book
                        bookISBN = readerFields.get(fieldIndex).trim();
                        readersBook = getBookByISBN(bookISBN);
                        dueDate = convertDate(readerFields.get(fieldIndex+1).trim(), Code.DATE_CONVERSION_ERROR);
                        if(dueDate == null) {
                            System.out.println("ERROR: Invalid date conversion.");
                            System.out.println("WARNING: Could not parse book being checked out due to error." +
                                    "  BOOK ENTRY DISCARDED.");
                            continue;
                        }

                        // check book out of library shelf and put into readers list
                        returnedCode = checkOutBook(newReader, readersBook);
                        System.out.println(returnedCode);
                        fieldIndex = fieldIndex + 2;
                    }
                }
            } else {
                return Code.UNKNOWN_ERROR;
            }
        }
        return Code.SUCCESS;
    }

    /**
     * This method addBook() adds the newBook into books(HashMap).  If no copy exists in books, the newBook
     * is added with value 1.  If a copy already exists in books, then the value of newBook is incremented.
     * NOTE: If the shelf the book belongs on does not exist, it is not added to that shelf until the shelf
     * is created.  This method calls addBookToShelf() if there is a shelf subject match with book subject.
     * @param newBook a Book to be added into books(HashMap)
     * @return a Code which indicates if there were any errors during processing. If none, Code.SUCCESS is returned.
     */
    public Code addBook(Book newBook) {
        if(!books.containsKey(newBook)) {
            books.put(newBook, 1);
            System.out.println(newBook + " added to the stacks.");
        } else {
            books.replace(newBook, books.get(newBook) + 1);
            System.out.println(books.get(newBook) + " copies of " + newBook + " in the stacks.");
        }

        if(shelves.containsKey(newBook.getSubject())) {
            addBookToShelf(newBook,shelves.get(newBook.getSubject()));
            return Code.SUCCESS;
        } else {
            System.out.println("No shelf for " + newBook.getSubject() + " books.");
            return Code.SHELF_EXISTS_ERROR;
        }
    }

    /**
     * This method returnBook() takes a reader and returns a book from their list back to the library's shelf.
     * @param reader the object returning a book from their book list
     * @param book the book object being returned to a shelf.
     * @return a Code which indicates if there were any errors during processing. If none, Code.SUCCESS is returned.
     */
    public Code returnBook(Reader reader, Book book) {
        Code returnedCode;

        // checks if reader has the book in their list
        if(!reader.hasBook(book)) {
            System.out.println(reader.getName() + " does not have " + book.getTitle() + " checked out.");
            return Code.READER_DOESNT_HAVE_BOOK_ERROR;
        }

        // removes book from reader's list
        System.out.println(reader.getName() + " is returning " + book);
        returnedCode = reader.removeBook(book);
        if(!returnedCode.equals(Code.SUCCESS)) {
            System.out.println("Could not return " + book);
        }

        // puts book back on shelf (assuming correct shelf subject exists)
        returnedCode = returnBook(book);

        return returnedCode;
    }

    /**
     * This method returnBook() takes the book object and adds it to the correct subject shelf if it exists.
     * @param book the book object to be placed back on the shelf it belong in the library or no shelf if subject
     *             does not exist.
     * @return a Code which indicates if there were any errors during processing. If none, Code.SUCCESS is returned.
     */
    public Code returnBook(Book book) {
        if(shelves.get(book.getSubject()).addBook(book).equals(Code.SUCCESS)) {
            return Code.SUCCESS;
        }
        System.out.println("No shelf for " + book);
        return Code.SHELF_EXISTS_ERROR;
    }

    /**
     * This method addBookToShelf() adds the passed in book to the passed in shelf after making sure that the
     * subjects match.  The book is added to the shelf using the shelf class .addBook() method.
     * @param book the new book entry to add to shelf
     * @param shelf the shelf to place book entry
     * @return a Code which indicates if there were any errors during processing. If none, Code.SUCCESS is returned.
     */
    private Code addBookToShelf(Book book, Shelf shelf) {
        Code returnedCode;

        if(book.getSubject().equals(shelf.getSubject())) {
            returnedCode = shelf.addBook(book);
            if(returnedCode != Code.SUCCESS) {
                System.out.println("Could not add " + book + " to shelf.");
            }
            return returnedCode;
        } else {
            System.out.println("Book and subject do not match.");
            return Code.SHELF_SUBJECT_MISMATCH_ERROR;
        }
    }

    /**
     * This method listBooks() displays all books at the library, even if they are not on a shelf.
     * @return integer number of books total in the library.
     */
    public int listBooks() {
        int bookCounter = 0;

        System.out.println("Displaying list of books.");
        for(Map.Entry<Book,Integer> book : books.entrySet()) {
            System.out.println(book.getValue() + " copies of " + book.getKey().toString());
            bookCounter =  bookCounter + book.getValue();
        }

        if(bookCounter == 0) {
            System.out.println("NOTE: No books to display.");
        }

        return bookCounter;
    }

    /**
     * This method checkOutBook() ensures that a valid reader and check out a valid book on a valid shelf where
     * at least one copy is still on the shelf.  If the validations all pass, a book is removed from shelf
     * and added to the readers list.
     * @param reader the reader who is checking out a book
     * @param book the book being checked out by the reader.  the book is added to reader's list.
     * @return a Code which indicates if there were any errors during processing. If none, Code.SUCCESS is returned.
     */
    public Code checkOutBook(Reader reader, Book book) {
        // Check if reader is invalid
        if(!readers.contains(reader)) {
            System.out.println("ERROR: " + reader + " doesn't have an account here.");
            return Code.READER_NOT_IN_LIBRARY_ERROR;
        }

        // Check if reader has exceeded lending limit
        if(reader.getBookCount() >= LENDING_LIMIT) {
            System.out.println("ERROR: " + reader.getName() + " has reached the lending limit of " + LENDING_LIMIT);
            return Code.BOOK_LIMIT_REACHED_ERROR;
        }

        // Check if book exists in books
        if(!books.containsKey(book)) {
            System.out.println("ERROR: Could not find " + book);
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }

        // Check if book has a shelf (subject shelf exists)
        if(!shelves.containsKey(book.getSubject())) {
            System.out.println("ERROR: No shelf for " + book.getSubject() + " books.");
            return Code.SHELF_EXISTS_ERROR;
        }

        // Check if shelf does have a copy available
        if(shelves.get(book.getSubject()).getBooks().get(book) < 1) {
            System.out.println("ERROR: No copies of " + book + " remains.");
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }

        // Remove book from shelf
        Shelf bookShelf = getShelf(book.getSubject());
        Code returnedCode = bookShelf.removeBook(book);
        if(returnedCode == Code.SUCCESS) {
            // Set due date for book using LOAN_PERIOD constant if one has not already been set
            if(book.getDueDate() != DEFAULT_DATE) {
                book.setDueDate(LocalDate.now().plusDays(LOAN_PERIOD));
            }

            // Use reader class method addBook()
            returnedCode = reader.addBook(book);
            if(returnedCode == Code.SUCCESS) {
                System.out.println(book + " checked out successfully.");
            } else {
                // If a book fails to be added to readers list, the book needs to be returned to the shelf.
                System.out.println("Couldn't check out " + book + ".");
                System.out.println(reader.getName() + " already has a copy of " + book.getTitle() + " checked out.");
                System.out.println("Placing " + book + " back on shelf.");
                returnBook(book);
            }
        } else {
            System.out.println("ERROR: Failed to remove " + book + " from " + bookShelf + ".");
        }

        return returnedCode;
    }

    /**
     * This method locates a book in books using the ISBN and returns the found book.  If no book with that ISBN
     * is found, a message is displayed and null is returned.
     * @param isbn a String isbn that should match a book object's isbn field.
     * @return returns a book that matches the isbn or null if no book with that isbn is found.
     */
    public Book getBookByISBN(String isbn) {
        String returnedISBN;
        for(Map.Entry<Book,Integer> book : books.entrySet()) {
            returnedISBN = book.getKey().getIsbn();
            if(returnedISBN.equals(isbn)) {
                return book.getKey();
            }
        }
        System.out.println("ERROR: Could not find a book with isbn: " + isbn);
        return null;
    }

    /**
     * This method listShelves() has two display types.  True displays each shelf along with all the books on
     * those shelves.  False displays only the shelf information.
     * @return a Code which indicates if there were any errors during processing. If none, Code.SUCCESS is returned.
     */
    public Code listShelves(boolean showBooks) {
        if(shelves.size() < 1) {
            System.out.println("No shelves exist.  Nothing to display.");
            return Code.SUCCESS;
        }

        if(showBooks) {
            System.out.println("Displaying the " + name + "'s shelf sections with a list of books on those shelves.");
            for(Map.Entry<String, Shelf> shelf : shelves.entrySet()) {
                System.out.print(shelf.getValue().listBooks());
            }
        } else {
            System.out.println("Displaying the " + name + "'s shelf sections.");
            for(Map.Entry<String, Shelf> shelf : shelves.entrySet()) {
                System.out.println(shelf.getValue().toString());
            }
        }
        return Code.SUCCESS;
    }

    /**
     * This method addShelf() takes a string subject and adds a new shelf if it does not already exist in shelves.
     * The shelf's index will be the shelves size + 1.
     * @param shelfSubject the String subject of a shelf to be added to shelves.
     * @return a Code which indicates if there were any errors during processing. If none, Code.SUCCESS is returned.
     */
    public Code addShelf(String shelfSubject) {
        if(shelves.containsKey(shelfSubject)) {
            System.out.println("ERROR: Shelf subject " + shelfSubject + " already exists.");
            return Code.SHELF_EXISTS_ERROR;
        } else {
            Shelf newShelf = new Shelf();
            newShelf.setShelfNumber(shelves.size() + 1);
            newShelf.setSubject(shelfSubject);

            return addShelf(newShelf);
        }
    }

    /**
     * This method addShelf() that takes a Shelf object parameter creates a new Shelf in shelves(HashMap) if
     * the subject of the newShelf does not already exist in shelves.  This method adds books that have matching
     * subjects onto the shelf.
     * NOTE: the newShelf passed in MUST already have its subject and shelfNumber set.
     * WARNING: DO NOT add books to shelf in initShelves() since this will cause a duplicate book error.  The
     * reason the books are added here is that this is a public method and new shelves can be created using
     * addShelf(String) method that calls this method.
     * @param newShelf a new Shelf to be added to shelves
     * @return a Code which indicates if there were any errors during processing. If none, Code.SUCCESS is returned.
     */
    public Code addShelf(Shelf newShelf) {
        if(shelves.containsKey(newShelf.getSubject())) {
            System.out.println("ERROR: Shelf [" + newShelf.getSubject() + "] already exists.");
            return Code.SHELF_EXISTS_ERROR;
        }
        shelves.put(newShelf.getSubject(), newShelf);

        // Add books that belong to this shelf
        for(Map.Entry<Book,Integer> book : books.entrySet()) {
            if(book.getKey().getSubject().equals(newShelf.getSubject())) {
                int bookValue = book.getValue();
                for(int num = 0; num < bookValue; num++) {
                    addBookToShelf(book.getKey(),newShelf);
                }
            }
        }

        return Code.SUCCESS;
    }

    /**
     * This method getShelf() uses an integer shelf value to find and return a shelf.
     * @param shelfNumber the integer shelf number wanted
     * @return the shelf with the shelfNumber
     */
    public Shelf getShelf(Integer shelfNumber) {
        int returnedSelfNumber;

        for(Map.Entry<String,Shelf> shelf : shelves.entrySet()) {
            returnedSelfNumber = shelf.getValue().getShelfNumber();
            if(returnedSelfNumber == shelfNumber) {
                return shelf.getValue();
            }
        }

        System.out.println("No shelf number " + shelfNumber + " found.");
        return null;
    }

    /**
     * This method getShelf() uses a shelf subject to find the shelf to return.
     * @param subject the string subject of a shelf being searched for
     * @return the shelf with the matching subject string
     */
    public Shelf getShelf(String subject) {
        if(!shelves.containsKey(subject)) {
            System.out.println("No shelf for " + subject + " books.");
            return null;
        }
        return shelves.get(subject);
    }

    /**
     * This method listsReaders() displays each reader and their library card number.
     * @return the integer number of readers in the list
     */
    public int listReaders() {
        return listReaders(true);
    }

    /**
     * This method listReaders() has two displays that can be toggled using the boolean.  If true, it will display
     * each reader and the books they have checked out.  If false, then the other listReaders() method will
     * display the names and their library card number.
     * @param showBooks a boolean toggle for type of display of readers
     * @return the integer number of readers in the list
     */
    public int listReaders(boolean showBooks) {
        if(showBooks) {
            for(int i = 0; i < readers.size(); i++) {
                System.out.println(readers.get(i));
            }
        } else {
            for(int i = 0; i < readers.size(); i++) {
                System.out.println(readers.get(i).getName() + " (#" + readers.get(i).getCardNumber() + ")");
            }
        }

        if(readers.size() == 0) {
            System.out.println("No readers to display.");
        }
        return readers.size();
    }

    /**
     * This method getReaderByCard() takes a reader account number and returns the reader object for that
     * person.  If the cardNumber is not valid, a message is displayed and null is returned.  No output is
     * displayed about the reader, unless it fails to find the reader.
     * @param cardNumber an integer value representing a reader's card number (unique)
     * @return the reader object that matches the cardNumber account.
     */
    public Reader getReaderByCard(int cardNumber) {
        for (Reader reader : readers) {
            if (reader.getCardNumber() == cardNumber) {
                return reader;
            }
        }
        System.out.println("Could not find a reader with card #" + cardNumber);
        return null;
    }

    /**
     * This method addReader() checks if the reader object already exists in the readers list or if there is
     * already another reader with the same library cardNumber.  In those cases, a message is displayed and
     * error is returned.  Otherwise, the reader is added to readers and the class libraryCard variable is
     * checked and updated if needed.
     * @param reader the new reader object to add to readers list if it is unique
     * @return a Code which indicates if there were any errors during processing. If none, Code.SUCCESS is returned.
     */
    public Code addReader(Reader reader) {
        if(readers.contains(reader)) {
            System.out.println("Error: " + reader.getName() + " already has an account!");
            return Code.READER_ALREADY_EXISTS_ERROR;
        }

        for (Reader value : readers) {
            if (reader.getCardNumber() == value.getCardNumber()) {
                System.out.println("ERROR: " + value.getName() + " and " + reader.getName()
                        + " have the same card number!");
                return Code.READER_CARD_NUMBER_ERROR;
            }
        }

        readers.add(reader);
//        System.out.println(reader.getName() + " added to the reader's list!");

        // updates the libraryCard number, which is the last used card number
        if(reader.getCardNumber() > libraryCard) {
            libraryCard = reader.getCardNumber();
        }

        return Code.SUCCESS;
    }

    /**
     * This method removeReader() will remove a reader from the readers list only if the reader has 0 books
     * checked out and is a valid reader at the library.  Otherwise, appropriate error messages will be
     * displayed.
     * @param reader the reader object to be removed from the library.
     * @return a Code which indicates if there were any errors during processing. If none, Code.SUCCESS is returned.
     */
    public Code removeReader(Reader reader) {
        for (Reader r : readers) {
            if (r == reader) {
                if(reader.getBookCount() > 0) {
                    System.out.println(reader.getName() + " must return all books before removing reader.");
                    return Code.READER_STILL_HAS_BOOKS_ERROR;
                } else {
                    readers.remove(reader);
                    System.out.println(reader.getName() + "(#" + reader.getCardNumber()
                            + ") has been removed from " + name + " library.");
                    return Code.SUCCESS;
                }
            }
        }
        System.out.println(reader.getName() + " is not a reader of " + name + " library.");
        return Code.READER_NOT_IN_LIBRARY_ERROR;
    }

    /**
     * This static method getLibraryCardNumber() returns the next library card value available for adding a new
     * reader.
     * NOTE: After new reader is successfully added, it should update the libraryCard number count by +1.
     * @return integer value of the next library card number available
     */
    public static int getLibraryCardNumber() {
        return libraryCard + 1;
    }

    /**
     * This method convertInt() parses the String input and converts it into an Integer.
     * FUTURE IMPROVEMENTS: Use enum String output for error Codes.  Create enum error Code -666.
     * @param recordCountString a String representation of an integer
     * @param code a Code type for the type of integer the conversion is for
     * @return an integer value for the original string passed in, or negative error value if there was an
     * error during conversion.
     */
    public static Integer convertInt(String recordCountString, Code code) {
        Integer recordCount = 0;
        try {
            recordCount = Integer.parseInt(recordCountString);
        } catch(NumberFormatException e) {
            System.out.println("   INVALID integer input: " + recordCountString);
            switch (code) {
                case BOOK_COUNT_ERROR -> System.out.println("ERROR: Could not read number of books.");
                case PAGE_COUNT_ERROR -> System.out.println("ERROR: Could not parse page count.");
                case DATE_CONVERSION_ERROR -> System.out.println("ERROR: Could not parse date component.");
                case SHELF_COUNT_ERROR -> System.out.println("ERROR: Could not read number of shelves.");
                case READER_COUNT_ERROR -> System.out.println("ERROR: Could not read number of readers.");
                default -> {
                    System.out.println("ERROR: Unknown conversion error.");
                    return -666; // Evil code.
                }
            }
            return code.getCode();
        }
        return recordCount;
    }

    /**
     * This method convertDate() takes a String and uses it to return a validated (or default) LocalDate.
     * RESOURCE: Used Oracle website documentation for LocalDate on how to parse and what exceptions
     * to catch.
     * @param date a String representation of a date that needs to be parsed for the return LocalDate
     * @param code DATE_CONVERSION_ERROR displayed if date parameter is not in proper YYYY-MM-DD or 0000 format.
     * @return a validated (or default) LocalDate in YYYY-MM-DD format.
     */
    public static LocalDate convertDate(String date, Code code) {
        if(date.equals("0000")) {
            // equivalent to a null date, return default date
            return DEFAULT_DATE;
        }

        List<String> dateFields = Arrays.asList(date.split("-"));

        // YYYY-MM-DD
        if(dateFields.size() == 3) {

            Code returnedCode;

            int year = convertInt(dateFields.get(0).trim(), Code.DATE_CONVERSION_ERROR);
            if(year < 0) {
                System.out.println("ERROR: Invalid year entry " + dateFields.get(0).trim());
                System.out.println("Using default date (YYYY-MM-DD): " + DEFAULT_DATE);
                return DEFAULT_DATE;
            }

            int month = convertInt(dateFields.get(1).trim(), Code.DATE_CONVERSION_ERROR);
            if(month < 0) {
                System.out.println("ERROR: Invalid month entry " + dateFields.get(1).trim());
                System.out.println("Using default date (YYYY-MM-DD): " + DEFAULT_DATE);
                return DEFAULT_DATE;
            }

            int day = convertInt(dateFields.get(2).trim(), Code.DATE_CONVERSION_ERROR);
            if(day < 0) {
                System.out.println("ERROR: Invalid day entry " + dateFields.get(2).trim());
                System.out.println("Using default date (YYYY-MM-DD): " + DEFAULT_DATE);
                return DEFAULT_DATE;
            }

            try {
                // The only way to get a return of a valid date conversion.
                return LocalDate.of(year, month, day);
            } catch (DateTimeException e) {
                if(year < 0) {
                    System.out.println("ERROR: Cannot convert Year [" + year + "]");
                }

                if(month < 0 || month > 12) {
                    System.out.println("ERROR: Cannot convert Month [" + month + "]");
                }

                if(day < 0 || day > 31) {
                    System.out.println("   ERROR: Cannot convert Day [" + day + "]");
                }

                System.out.println("   Using default date (YYYY-MM-DD): " + DEFAULT_DATE);
//                e.printStackTrace();

                // invalid date cannot be converted, so return default date
                return DEFAULT_DATE;
            } // end try/catch block
        } // end if(dateFields.size() == 3) block

        System.out.println("ERROR: " + code.getCode() + ", could not parse " + date);
        System.out.println("Using default date (YYYY-MM-DD): " + DEFAULT_DATE);
        // date parameter passed in was not in the correct format of YYYY-MM-DD
        return DEFAULT_DATE;
    }

    /**
     * This method errorCode() takes a code number and returns the enum Code for it.
     * @param codeNumber an integer representation of a Code enum.  The error code value.
     * @return a Code enum for the integer error code value
     */
    private Code errorCode(int codeNumber) {
        for(Code code: Code.values()) {
            if(code.getCode() == codeNumber) {
                return code;
            }
        }
        return Code.UNKNOWN_ERROR;
    }

    /**
     * This method errorMessage() takes an integer codeNumber and returns the string value of that Code enum.
     * @param codeNumber an integer representation of a Code enum.  The error code value.
     * @return a String for the enum Code
     */
    private static String errorMessage(int codeNumber) {
        for (Code code : Code.values()) {
            if (code.getCode() == codeNumber) {
                return code.getMessage();
            }
        }
        return Code.UNKNOWN_ERROR.getMessage();
    }
}
