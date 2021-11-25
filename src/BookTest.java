import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

/**
 * title: BookTest.java
 * abstract: This class BookTest() is a jUnit test for Book.java.
 * name: Juli S
 * date: 11/06/2021
 */

class BookTest {
    // These variables are constants so that hey are not manipulated during the test since they are used for
    // validating tests.
    private static final String ISBN = "1337";
    private static final String TITLE = "Headfirst Java";
    private static final String SUBJECT = "education";
    private static final int PAGE_COUNT = 1337;
    private static final String AUTHOR = "Grady Booch";
    private static final LocalDate DUE_DATE = LocalDate.now();
    private static final String OUTPUT_STRING = TITLE + " by " + AUTHOR + " ISBN: " + ISBN;

    // This Book object should not be changed during the test, but instead used to validate test results.
    Book testBook = new Book(ISBN, TITLE, SUBJECT, PAGE_COUNT, AUTHOR, DUE_DATE);

    @Test
    void constructorTest() {
        Book newBook = null;
        assertNull(newBook);
        newBook = new Book(ISBN, TITLE, SUBJECT, PAGE_COUNT, AUTHOR, DUE_DATE);
        assertNotNull(newBook);
    }

    @Test
    void getIsbn() {
        assertEquals(ISBN,testBook.getIsbn());
    }

    @Test
    void setIsbn() {
        Book newBook = new Book("","","",0,"",null);
        newBook.setIsbn(ISBN);
        assertEquals(ISBN, newBook.getIsbn());
    }

    @Test
    void getTitle() {
        assertEquals(TITLE,testBook.getTitle());
    }

    @Test
    void setTitle() {
        Book newBook = new Book("","","",0,"",null);
        newBook.setTitle(TITLE);
        assertEquals(TITLE, newBook.getTitle());
    }

    @Test
    void getSubject() {
        assertEquals(SUBJECT,testBook.getSubject());
    }

    @Test
    void setSubject() {
        Book newBook = new Book("","","",0,"",null);
        newBook.setSubject(SUBJECT);
        assertEquals(SUBJECT, newBook.getSubject());
    }

    @Test
    void getPageCount() {
        assertEquals(PAGE_COUNT,testBook.getPageCount());
    }

    @Test
    void setPageCount() {
        Book newBook = new Book("","","",0,"",null);
        newBook.setPageCount(PAGE_COUNT);
        assertEquals(PAGE_COUNT, newBook.getPageCount());
    }

    @Test
    void getAuthor() {
        assertEquals(AUTHOR,testBook.getAuthor());
    }

    @Test
    void setAuthor() {
        Book newBook = new Book("","","",0,"",null);
        newBook.setAuthor(AUTHOR);
        assertEquals(AUTHOR, newBook.getAuthor());
    }

    @Test
    void getDueDate() {
        assertEquals(DUE_DATE,testBook.getDueDate());
    }

    @Test
    void setDueDate() {
        Book newBook = new Book("","","",0,"",null);
        newBook.setDueDate(DUE_DATE);
        assertEquals(DUE_DATE, newBook.getDueDate());
    }

    @Test
    void testEquals() {
        Book newBook = new Book("","","",0,"",null);
        assertNotEquals(testBook, newBook);
        newBook = new Book(ISBN, TITLE, SUBJECT, PAGE_COUNT, AUTHOR, DUE_DATE);
        assertEquals(testBook,newBook);
    }

    @Test
    void testToString() {
        assertEquals(testBook.toString(),OUTPUT_STRING);
    }
}