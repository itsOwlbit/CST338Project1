import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

/**
 * title: ShelfTest.java
 * abstract: This class ShelfTest() is a jUnit test for Shelf.java.
 * name: Juli S
 * date: 11/08/2021
 */

class ShelfTest {
    private static final String ISBN = "1337";
    private static final String TITLE = "Headfirst Java";
    private static final String SUBJECT = "education";
    private static final int PAGE_COUNT = 1337;
    private static final String AUTHOR = "Grady Booch";
    private static final LocalDate DUE_DATE = LocalDate.now();
    private static final String OUTPUT_STRING = TITLE + " by " + AUTHOR + " ISBN: " + ISBN;

    Book testBook = new Book(ISBN, TITLE, SUBJECT, PAGE_COUNT, AUTHOR, DUE_DATE);
    Book failBook = new Book("34-w-34", "Dune", "sci-fi", 235, "Frank Herbert", null);

    Random random = new Random();
    int randomInt = random.nextInt(10);
    HashMap<Book, Integer> testBooks = new HashMap<Book, Integer>() {{put(testBook, randomInt);}};

    @Test
    void constructorTest() {
        Shelf s = null;
        assertNull(s);
        s = new Shelf();
        assertNotNull(s);
    }

    @Test
    void getBookCount() {
        Shelf s = new Shelf();
        s.setSubject(failBook.getSubject());
        int testCount = randomInt;
        s.setBooks(testBooks); // create a Book with random number of copies
        assertEquals(testCount, s.getBookCount(testBook)); // expected and actual counts should match
        s.removeBook(testBook);
        testCount --;
        assertEquals(testCount,s.getBookCount(testBook)); // one book removed and count tested
        while(testBooks.get(testBook) > 0) {
            s.removeBook(testBook);
            testCount --;
        }
        assertEquals(0,s.getBookCount(testBook)); // removed all book and count tested (should be 0)
        assertEquals(-1, s.getBookCount(failBook)); // testing count on book that isn't on this shelf
    }

    @Test
    void addBook() {
        Shelf s = new Shelf();
        s.setSubject(SUBJECT);
        assertEquals(Code.SUCCESS, s.addBook(testBook)); // same subject book as shelf
        assertEquals(1, s.getBookCount(testBook)); // expected and actual counts should match
        assertEquals(Code.SUCCESS, s.addBook(testBook));
        assertEquals(2, s.getBookCount(testBook));
        assertEquals(Code.SHELF_SUBJECT_MISMATCH_ERROR, s.addBook(failBook)); // book with different subject than shelf
        assertEquals(-1, s.getBookCount(failBook));
    }

    @Test
    void removeBook() {
        Shelf s = new Shelf();
        s.setSubject(SUBJECT);
        assertEquals(Code.BOOK_NOT_IN_INVENTORY_ERROR, s.removeBook(testBook));
        assertEquals(Code.SUCCESS, s.addBook(testBook));
        assertEquals(1, s.getBookCount(testBook));
        assertEquals(Code.SUCCESS, s.addBook(testBook));
        assertEquals(2, s.getBookCount(testBook));
        assertEquals(Code.SUCCESS, s.removeBook(testBook));
        assertEquals(1, s.getBookCount(testBook));
        assertEquals(Code.SUCCESS, s.removeBook(testBook));
        assertEquals(0, s.getBookCount(testBook));
        assertEquals(Code.BOOK_NOT_IN_INVENTORY_ERROR, s.removeBook(testBook));
        assertEquals(0, s.getBookCount(testBook));
    }

    @Test
    void listBooks() {
        Shelf s = new Shelf();
        s.setSubject(SUBJECT);
        assertEquals(Code.SUCCESS, s.addBook(testBook)); // same subject book as shelf
        assertEquals(1, s.getBookCount(testBook)); // expected and actual counts should match
        System.out.println();
        String expectedOutput = "1 books on shelf: 0 : " + SUBJECT  + "\n" + testBook.toString();
        System.out.println(s.listBooks());
        assertEquals(expectedOutput, s.listBooks());
    }

    @Test
    void getShelfNumber() {
        Shelf s = new Shelf();
        s.setSubject(SUBJECT);
        assertEquals(0, s.getShelfNumber());
    }

    @Test
    void setShelfNumber() {
        Shelf s = new Shelf();
        s.setSubject(SUBJECT);
        s.setShelfNumber(1);
        assertEquals(1, s.getShelfNumber());
    }

    @Test
    void getSubject() {
        Shelf s = new Shelf();
        assertNotEquals(SUBJECT, s.getSubject());
        s.setSubject(SUBJECT);
        assertEquals(SUBJECT, s.getSubject());
    }

    @Test
    void setSubject() {
        Shelf s = new Shelf();
        assertNotEquals(SUBJECT, s.getSubject());
        s.setSubject(SUBJECT);
        assertEquals(SUBJECT, s.getSubject());
    }

    @Test
    void getBooks() {
        Shelf s = new Shelf();
        assertNotEquals(testBooks, s.getBooks());
        s.setBooks(testBooks);
        assertEquals(testBooks, s.getBooks());
    }

    @Test
    void setBooks() {
        Shelf s = new Shelf();
        assertNotEquals(testBooks, s.getBooks());
        s.setBooks(testBooks);
        assertEquals(testBooks, s.getBooks());
    }

    @Test
    void testEquals() {
        Shelf s = new Shelf();
        s.setSubject(SUBJECT);
        Shelf other = new Shelf();
        other.setSubject("sci-fi");
        assertNotEquals(other, s);
        other = s;
        assertEquals(other, s);
    }

    @Test
    void testToString() {
        Shelf s = new Shelf();
        s.setSubject((SUBJECT));
        assertEquals("0 : education", s.toString());
    }
}