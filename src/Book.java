import java.time.LocalDate;
import java.util.Objects;

/**
 * title: Book.java
 * abstract: This class Book is used to create book objects that will be part of the Library Project.  The
 * Library Project has 4 classes: Library, Shelf, Book, and Reader.  These classes will be used to store,
 * manipulate, and analyse information stored in the library.
 * name: Juli S
 * date: 11/06/2021
 *
 * NOTE: The library .cvs file used to create Book objects MUST have fields in the correct order delimited by a
 *       comma(,).
 *       Format example: isbn,title,subject,pageCount,author,dueDate
 */

public class Book {
    // Constant Fields: used to index the values from the library .cvs files.
    public static final int ISBN_ = 0;
    public static final int TITLE_ = 1;
    public static final int SUBJECT_ = 2;
    public static final int PAGE_COUNT_ = 3;
    public static final int AUTHOR_ = 4;
    public static final int DUE_DATE_ = 5;

    // Variable Fields: used for each Book object
    private String isbn;
    private String title;
    private String subject;
    private int pageCount;
    private String author;
    private LocalDate dueDate;

    public Book(String isbn, String title, String subject, int pageCount, String author, LocalDate dueDate) {
        this.isbn = isbn;
        this.title = title;
        this.subject = subject;
        this.pageCount = pageCount;
        this.author = author;
        this.dueDate = dueDate;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    /**
     * This equals() method compares everything except the dueDate.
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return pageCount == book.pageCount && isbn.equals(book.isbn) && title.equals(book.title)
                         && subject.equals(book.subject) && author.equals(book.author);
    }

    @Override
    /**
     * This hashCode() methodwill compare everything except the dueDate.
     */
    public int hashCode() {
        return Objects.hash(isbn, title, subject, pageCount, author);
    }

    @Override
    /**
     * This toString() method returns the below formatted String:
     *      [title] by [author] ISBN: [isbn]
     */
    public String toString() {
        return title + " by " + author + " ISBN: " + isbn;
    }
}
