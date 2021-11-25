import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * title: ReaderTest.java
 * abstract: This class ReaderTest() is a jUnit test for Reader.java.
 * name: Juli S
 * date: 11/06/2021
 */

class ReaderTest {

    private static final int CARD_NUMBER = 2187;
    private static final String NAME = "Bob Barker";
    private static final String PHONE = "123-456-7890";
    private List<Book> books;

    // This Book object should not be changed during the test, but instead used to validate test results.
    Book testBook = new Book("1337", "Headfirst Java", "education", 1337,
                            "Grady Booch", LocalDate.now());

    private static final String OUTPUT_STRING = "Bob Barker(#2187) has checked out {[]}";

    @Test
    void constructorTest() {
        Reader reader = null;
        assertNull(reader);
        reader = new Reader(CARD_NUMBER, NAME, PHONE);
        assertNotNull(reader);
        reader.addBook(testBook);
        assertNull(books);
    }

    @Test
    void addBook() {
        Reader reader = new Reader(CARD_NUMBER, NAME, PHONE);
        assertEquals(reader.addBook(testBook), Code.SUCCESS);
        assertEquals(reader.addBook(testBook), Code.BOOK_ALREADY_CHECKED_OUT_ERROR);
    }

    @Test
    void removeBook() {
        Reader reader = new Reader(CARD_NUMBER, NAME, PHONE);
        reader.addBook(testBook);
        assertEquals(reader.removeBook(testBook), Code.SUCCESS);
        assertEquals(reader.removeBook(testBook), Code.READER_COULD_NOT_REMOVE_BOOK_ERROR);
    }

    @Test
    void hasBook() {
        Reader reader = new Reader(CARD_NUMBER, NAME, PHONE);
        assertFalse(reader.hasBook(testBook));
        reader.addBook(testBook);
        assertTrue(reader.hasBook(testBook));
    }

    @Test
    void getBookCount() {
        Reader reader = new Reader(CARD_NUMBER, NAME, PHONE);
        assertEquals(0, reader.getBookCount());
        reader.addBook(testBook);
        assertEquals(1, reader.getBookCount());
        reader.removeBook(testBook);
        assertEquals(0, reader.getBookCount());
    }

    @Test
    void getBooks() {
        Reader reader = new Reader(CARD_NUMBER, NAME, PHONE);
        books = new ArrayList<>();
        reader.addBook(testBook);
        books.add(testBook);
        assertEquals(books, reader.getBooks());
    }

    @Test
    void setBooks() {
        Reader reader = new Reader(CARD_NUMBER, NAME, PHONE);
        books = new ArrayList<>();
        reader.setBooks(books);
        assertEquals(books, reader.getBooks());
    }

    @Test
    void getCardNumber() {
        Reader reader = new Reader(CARD_NUMBER, NAME, PHONE);
        assertEquals(CARD_NUMBER, reader.getCardNumber());
    }

    @Test
    void setCardNumber() {
        Reader reader = new Reader(CARD_NUMBER, NAME, PHONE);
        reader.setCardNumber(12345);
        assertNotEquals(CARD_NUMBER, reader.getCardNumber());
        assertEquals(12345, reader.getCardNumber());
    }

    @Test
    void getName() {
        Reader reader = new Reader(CARD_NUMBER, NAME, PHONE);
        assertEquals(NAME, reader.getName());
    }

    @Test
    void setName() {
        Reader reader = new Reader(CARD_NUMBER, NAME, PHONE);
        reader.setName("Jane Doe");
        assertNotEquals(NAME, reader.getName());
        assertEquals("Jane Doe", reader.getName());
    }

    @Test
    void getPhone() {
        Reader reader = new Reader(CARD_NUMBER, NAME, PHONE);
        assertEquals(PHONE, reader.getPhone());
    }

    @Test
    void setPhone() {
        Reader reader = new Reader(CARD_NUMBER, NAME, PHONE);
        reader.setPhone("098-765-4321");
        assertNotEquals(PHONE, reader.getPhone());
        assertEquals("098-765-4321", reader.getPhone());
    }

    @Test
    void testEquals() {
        Reader reader = new Reader(CARD_NUMBER, NAME, PHONE);
        Reader otherReader = new Reader(123, "Jane Doe", "837-825-0742");
        assertNotEquals(reader, otherReader);
        otherReader.setCardNumber(reader.getCardNumber());
        otherReader.setName(reader.getName());
        otherReader.setPhone(reader.getPhone());
        otherReader.setBooks(reader.getBooks());
        assertEquals(reader, otherReader);
    }

    @Test
    void testToString() {
        Reader reader = new Reader(CARD_NUMBER, NAME, PHONE);
        assertEquals(OUTPUT_STRING, reader.toString());
    }
}