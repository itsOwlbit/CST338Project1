import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

/**
 * title: LibraryTest.java
 * abstract: This class is the jUnit test for Library.java.  It only tests public methods and does its best
 * to test the private methods by targeting different scenarios.
 * author: Juli S
 * date: 11/14/2021
 *
 * NOTE: Requires Fail.csv, LibraryTest00.csv, LibraryTest01.csv, LibraryTest02.csv, and LibraryTest03.csv files.
 */

class LibraryTest {

    @Test
    void constructorTest() {
        System.out.println("TEST from constructorTest()");
        Library newLibrary = null;
        assertNull(newLibrary);
        newLibrary = new Library("OtterBot");
        assertNotNull(newLibrary);
        System.out.println("END TEST");
        System.out.println();
    }

    // NOTE: This test REQUIRES 4 test files.  LibraryTest00.csv - LibraryTest03.csv
    // UNCOMMENT THE BLOCK SECTION BELOW IF YOU HAVE THE FILES in the same directory as the src folder.
    // NOT in src folder.
/*
    @Test
    void initTest() {
        System.out.println("TEST from initTest()");
        Library newLibrary = new Library("OtterBot");
        // Test a file that does not exist
        assertEquals(Code.FILE_NOT_FOUND_ERROR, newLibrary.init("Fail.csv"));

        //  Test a file that does exist and is correct
        assertEquals(Code.SUCCESS, newLibrary.init("LibraryTest00.csv"));

        // Test a file that does exist, but has error bookCount value.
        Library failLibrary = new Library("Fail's Library");
        assertEquals(Code.BOOK_COUNT_ERROR, failLibrary.init("LibraryTest01.csv"));

        // Test a file that does exist, but has error shelfCount value.
        failLibrary = new Library("Fail's Library");
        assertEquals(Code.SHELF_COUNT_ERROR, failLibrary.init("LibraryTest02.csv"));

        // Test a file that does exist, but has error readerCount value.
        failLibrary = new Library("Fail's Library");
        assertEquals(Code.READER_COUNT_ERROR, failLibrary.init("LibraryTest03.csv"));

        System.out.println("END TEST");
        System.out.println();
    }
*/

    @Test
    // This also tests addBookToShelf() which is a private method
    void addBookTest() {
        System.out.println("TEST from addBookTest()");
        Library newLibrary = new Library("OtterBot");
        LocalDate dueDate = Library.convertDate("0000", Code.DATE_CONVERSION_ERROR);
        Book newBook = new Book("1337", "Headfirst Java", "education", 1337,
                "Grady Booch", dueDate);
        assertEquals(Code.SHELF_EXISTS_ERROR, newLibrary.addBook(newBook));

        // Create new shelf with subject to add same book to check for Code.SUCCESS return.
        // NOTE: This test does not care that one book is not on shelf and another copy on shelf.
        Shelf shelf = new Shelf();
        shelf.setSubject("education");
        newLibrary.addShelf(shelf);
        assertEquals(Code.SUCCESS, newLibrary.addBook(newBook));

        System.out.println("END TEST");
        System.out.println();
    }

    @Test
    void listBooksTest() {
        System.out.println("TEST from listBooksTest()");
        Library newLibrary = new Library("OtterBot");
        LocalDate dueDate = Library.convertDate("0000", Code.DATE_CONVERSION_ERROR);
        Book newBook = new Book("1337", "Headfirst Java", "education", 1337,
                "Grady Booch", dueDate);

        // No books to list
        assertEquals(0, newLibrary.listBooks());

        // List one book that is added to books.
        newLibrary.addBook(newBook);
        assertEquals(1, newLibrary.listBooks());

        System.out.println("END TEST");
        System.out.println();
    }

    @Test
    void checkOutAndReturnBookTest() {
        System.out.println("TEST from checkOutAndReturnBookTest()");
        Library newLibrary = new Library("OtterBot");
        LocalDate dueDate = Library.convertDate("0000", Code.DATE_CONVERSION_ERROR);
        Book bookOne = new Book("1337", "Headfirst Java", "education", 1337,
                "Grady Booch", dueDate);
        Book bookTwo = new Book("42-w-87", "Hitchhikers Guide To the Galaxy", "sci-fi", 42,
                "Douglas Adams", dueDate);
        Book bookThree = new Book("34-w-34", "Dune", "sci-fi", 235,
                "Frank Herbert", dueDate);
        Book bookFour = new Book("isbnFour", "titleFour", "sci-fi", 4,
                "authorFour", dueDate);
        Book bookFive = new Book("isbnFive", "titleFive", "sci-fi", 5,
                "authorFive", dueDate);
        Book bookSix = new Book("isbnSix", "TitleSix", "sci-fi", 6,
                "authorSix", dueDate);
        newLibrary.addBook(bookOne);
        newLibrary.addBook(bookTwo);

        Shelf shelf = new Shelf();
        shelf.setSubject("sci-fi");
        shelf.setShelfNumber(1);
        newLibrary.addShelf(shelf);
        Reader reader = new Reader(1, "Juli S.", "123-456-7890");

        // Testing reader's validity
        assertEquals(Code.READER_NOT_IN_LIBRARY_ERROR, newLibrary.checkOutBook(reader,bookOne));

        // Test if book exists in books
        newLibrary.addReader(reader);
        assertEquals(Code.BOOK_NOT_IN_INVENTORY_ERROR, newLibrary.checkOutBook(reader,bookThree));
        newLibrary.addBook(bookThree);

        // Test if book's subject shelf exists
        assertEquals(Code.SHELF_EXISTS_ERROR, newLibrary.checkOutBook(reader,bookOne));
        newLibrary.addShelf("education");

        // Test removal of book from shelf and into reader's list of books
        Reader catReader = new Reader(2, "Momo", "098-765-4321");
        newLibrary.addReader(catReader);
        assertEquals(Code.SUCCESS,newLibrary.checkOutBook(catReader,bookOne));
        newLibrary.listShelves(true);
        // Test if a copy of book is available on shelf
        assertEquals(Code.BOOK_NOT_IN_INVENTORY_ERROR, newLibrary.checkOutBook(reader,bookOne));

//        System.out.println("\nCHECK HERE");
//        newLibrary.listReaders(true);
//        newLibrary.listShelves(true);
//        newLibrary.listBooks();

        // Test reader cannot have two copies of same book
        newLibrary.addBook(bookOne);
        assertEquals(Code.BOOK_ALREADY_CHECKED_OUT_ERROR, newLibrary.checkOutBook(catReader,bookOne));

        // Test another reader checking out the 2nd copy of the book and a third reader trying to get a copy
        assertEquals(Code.SUCCESS,newLibrary.checkOutBook(reader,bookOne));
        Reader dogReader = new Reader(3, "Kira", "666-666-6666");
        newLibrary.addReader(dogReader);
        assertEquals(Code.BOOK_NOT_IN_INVENTORY_ERROR, newLibrary.checkOutBook(dogReader,bookOne));

//        newLibrary.listReaders(true);
//        newLibrary.listShelves(true);
//        newLibrary.listBooks();
//        System.out.println("CHECK HERE\n");

        // Test for reader's lending limit
        assertEquals(Code.SUCCESS,newLibrary.checkOutBook(catReader,bookTwo));
        assertEquals(Code.SUCCESS,newLibrary.checkOutBook(catReader,bookThree));
        newLibrary.addBook(bookFour);
        newLibrary.addBook(bookFive);
        newLibrary.addBook(bookSix);
        assertEquals(Code.SUCCESS,newLibrary.checkOutBook(catReader,bookFour));
        assertEquals(Code.SUCCESS,newLibrary.checkOutBook(catReader,bookFive));
        assertEquals(Code.BOOK_LIMIT_REACHED_ERROR,newLibrary.checkOutBook(catReader,bookSix));
        newLibrary.listReaders(true);
        newLibrary.listShelves(true);

        // Return book tests
        assertEquals(Code.SUCCESS,newLibrary.returnBook(catReader,bookFive));
        assertEquals(Code.SUCCESS,newLibrary.returnBook(catReader,bookFour));
        assertEquals(Code.SUCCESS,newLibrary.returnBook(catReader,bookThree));
        assertEquals(Code.SUCCESS,newLibrary.returnBook(catReader,bookTwo));
        assertEquals(Code.SUCCESS,newLibrary.returnBook(catReader,bookOne));
        assertEquals(Code.READER_DOESNT_HAVE_BOOK_ERROR,newLibrary.returnBook(catReader,bookFive));
        assertEquals(Code.SUCCESS,newLibrary.returnBook(reader,bookOne));

        // output lists for visual proof
        newLibrary.listReaders(true); // 3 readers no books
        newLibrary.listShelves(true); // 2 shelves sci-fi (5 books) and education (2 Headfirst books)
        newLibrary.listBooks(); // 5 books with 1 copy each and Headfirst Java with 2 copies.

        System.out.println("END TEST");
        System.out.println();
    }

    @Test
    void getBookByISBNTest() {
        System.out.println("TEST from getBookByISBNTest()");
        Library newLibrary = new Library("OtterBot");
        LocalDate dueDate = Library.convertDate("0000", Code.DATE_CONVERSION_ERROR);
        Book newBook = new Book("1337", "Headfirst Java", "education", 1337,
                "Grady Booch", dueDate);

        // book not in library so should not show up
        assertEquals(null,newLibrary.getBookByISBN("1337"));

        // book is in library and should be found
        newLibrary.addBook(newBook);
        assertEquals(newBook,newLibrary.getBookByISBN("1337"));

        System.out.println("END TEST");
        System.out.println();
    }

    @Test
    // This test is for listShelves(), both addShelf(), and both getShelf()
    void shelvesTest() {
        System.out.println("TEST from shelvesTest()");
        Library newLibrary = new Library("OtterBot");
        LocalDate dueDate = Library.convertDate("0000", Code.DATE_CONVERSION_ERROR);
        Book newBook = new Book("1337", "Headfirst Java", "education", 1337,
                "Grady Booch", dueDate);

        // empty shelf
        assertEquals(Code.SUCCESS,newLibrary.listShelves(true));

        // testing getShelf() methods with shelf that don't exist
        assertEquals(null, newLibrary.getShelf("education"));
        assertEquals(null, newLibrary.getShelf(1));

        // one shelf exists
        Shelf shelf = new Shelf();
        shelf.setSubject("education");
        shelf.setShelfNumber(1);
        newLibrary.addShelf(shelf);
        newLibrary.addShelf("sci-fi");

        // trying to add a shelf that already exists
        assertEquals(Code.SHELF_EXISTS_ERROR, newLibrary.addShelf(shelf));
        assertEquals(Code.SHELF_EXISTS_ERROR, newLibrary.addShelf("education"));

        // display both types of listShelves
        assertEquals(Code.SUCCESS,newLibrary.listShelves(true));
        assertEquals(Code.SUCCESS,newLibrary.listShelves(false));

        // testing getShelf() methods
        assertEquals(shelf, newLibrary.getShelf("education"));
        assertEquals(shelf, newLibrary.getShelf(1));
        assertEquals(newLibrary.getShelf("sci-fi"), newLibrary.getShelf(2));
        assertEquals(null, newLibrary.getShelf(3));

        System.out.println("END TEST");
        System.out.println();
    }

    @Test
    // Tests both listReaders(), getReaderByCard(), addReader(), and removeReader() methods
    void readersTest() {
        System.out.println("TEST from readersTest()");
        Library newLibrary = new Library("OtterBot");
        LocalDate dueDate = Library.convertDate("0000", Code.DATE_CONVERSION_ERROR);
        Book newBook = new Book("1337", "Headfirst Java", "education", 1337,
                "Grady Booch", dueDate);
        newLibrary.addBook(newBook);
        Shelf shelf = new Shelf();
        shelf.setSubject("education");
        shelf.setShelfNumber(1);
        newLibrary.addShelf(shelf);

        // test both listReaders() with no readers
        assertEquals(0, newLibrary.listReaders());
        assertEquals(0, newLibrary.listReaders(true));
        assertEquals(0, newLibrary.listReaders(false));

        // testing getReaderByCard() on reader that does not exist, then addReader(), then again test getReaderByCard()
        assertEquals(null, newLibrary.getReaderByCard(1));
        Reader reader = new Reader(1, "Juli S", "123-456-7890");
        assertEquals(Code.SUCCESS, newLibrary.addReader(reader));
        assertEquals(reader, newLibrary.getReaderByCard(1));

        // test addReader() on an already existing reader.
        assertEquals(Code.READER_ALREADY_EXISTS_ERROR, newLibrary.addReader(reader));

        // test addReader() with a new reader with the same cardNumber as an already existing reader
        Reader readerCat = new Reader(1, "Momo", "098-765-4321");
        assertEquals(Code.READER_CARD_NUMBER_ERROR, newLibrary.addReader(readerCat));

        // test both listReaders() with one reader
        assertEquals(1, newLibrary.listReaders());
        assertEquals(1, newLibrary.listReaders(false));
        newLibrary.checkOutBook(reader, newBook);
        assertEquals(1, newLibrary.listReaders(true));

        // tests removing a reader
        assertEquals(Code.READER_STILL_HAS_BOOKS_ERROR, newLibrary.removeReader(reader));
        readerCat = new Reader(2, "Momo", "098-765-4321");
        newLibrary.addReader(readerCat);
        assertEquals(Code.SUCCESS, newLibrary.removeReader(readerCat));
        assertEquals(Code.READER_NOT_IN_LIBRARY_ERROR, newLibrary.removeReader(readerCat));

        System.out.println("END TEST");
        System.out.println();
    }

    @Test
    void convertIntTest() {
        System.out.println("TEST from convertIntTest()");
        assertEquals(2, Library.convertInt("2", Code.BOOK_COUNT_ERROR));
        assertEquals(Code.BOOK_COUNT_ERROR.getCode(),
                Library.convertInt("3f", Code.BOOK_COUNT_ERROR));
        assertEquals(Code.PAGE_COUNT_ERROR.getCode(),
                Library.convertInt("7s", Code.PAGE_COUNT_ERROR));
        assertEquals(Code.DATE_CONVERSION_ERROR.getCode(),
                Library.convertInt("seven", Code.DATE_CONVERSION_ERROR));
        assertEquals(-666, Library.convertInt("zero", Code.SUCCESS));
        System.out.println("END TEST");
        System.out.println();
    }

    @Test
    void convertDateTest() {
        System.out.println("TEST from convertDateTest()");
        LocalDate defaultDate = LocalDate.of(1970, 1, 1);

        // Input 0000 should have default date
        String dateInput = "0000";
        LocalDate convertedDate = Library.convertDate(dateInput, Code.DATE_CONVERSION_ERROR);
        assertTrue(defaultDate.equals(convertedDate));

        // Input missing 3 fields results in default date
        dateInput = "1834-02";
        convertedDate = Library.convertDate(dateInput, Code.DATE_CONVERSION_ERROR);
        assertTrue(defaultDate.equals(convertedDate));

        // Input is correct and should output exactly the same
        dateInput = "2021-11-14";
        convertedDate = Library.convertDate(dateInput, Code.DATE_CONVERSION_ERROR);
        assertTrue(LocalDate.of(2021, 11, 14).equals(convertedDate));

        // Input has incorrect values for YYYY, MM, and DD resulting in default date
        dateInput = "11-90-66";
        convertedDate = Library.convertDate(dateInput, Code.DATE_CONVERSION_ERROR);
        assertTrue(defaultDate.equals(convertedDate));

        // One input is a non-integer value
        dateInput = "2021-1f-14";
        convertedDate = Library.convertDate(dateInput, Code.DATE_CONVERSION_ERROR);
        assertTrue(defaultDate.equals(convertedDate));

        System.out.println("END TEST");
        System.out.println();
    }
}