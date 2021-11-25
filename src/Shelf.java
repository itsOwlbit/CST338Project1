import java.util.HashMap;
import java.util.Objects;

/**
 * title: Shelf.java
 * abstract: This class Shelf is used to create a shelf in a library where books are stored that a reader
 * can interact with by checking out and returning Books from the Library.  The Library Project has 4 classes:
 * Library, Shelf, Book, and Reader.  These classes will be used to store, manipulate, and analyse information
 * stored in the library.
 * name: Juli S
 * date: 11/07/2021
 */

public class Shelf {
    public static final int SHELF_NUMBER_ = 0;
    public static final int SUBJECT_ = 1;

    private int shelfNumber;
    private String subject;
    private HashMap<Book, Integer> books;

    public Shelf() {
        shelfNumber = 0;
        subject = "Not set";
        books = new HashMap<>();
    }

    public int getBookCount(Book book) {
        if(books.containsKey(book)) {
            return books.get(book);
        } else {
            return -1;
        }
    }

    public Code addBook(Book book) {
        if(books.containsKey(book)) {
            books.replace(book, books.get(book), books.get(book) +1);
            System.out.println(book + " added to shelf " + this);
            return Code.SUCCESS;
        } else if (this.subject.equals(book.getSubject())){
            books.put(book, 1);
            System.out.println(book + " added to shelf " + this);
            return Code.SUCCESS;
        } else {
            return Code.SHELF_SUBJECT_MISMATCH_ERROR;
        }
    }

    public Code removeBook(Book book) {
        if(books.containsKey(book)) {
            if(books.get(book) > 0) {
                books.replace(book, books.get(book) - 1);
                System.out.println(book + " successfully removed from shelf " + subject);
                return Code.SUCCESS;
            } else {
                System.out.println("No copies of " + book.getTitle() + " remain on shelf " + subject);
                return Code.BOOK_NOT_IN_INVENTORY_ERROR;
            }

        } else {
            System.out.println(book.getTitle() + "is not on the shelf " + subject);
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }
    }

    public String listBooks() {
        StringBuilder shelfOutput = new StringBuilder();
        int numOfBooks = 0; // used to store total number of books on the shelf

        // find total number of books on shelf for output
        for(Integer i : books.values()) {
            numOfBooks += i;
        }

        // Display shelf information and list all books on shelf
        shelfOutput.append("Shelf #" + this + " (has " + numOfBooks + " books)\n");
        for(Book book: books.keySet()) {
            shelfOutput.append(" > " + book.toString() + " (" + books.get(book) + ")\n");
        }

        return shelfOutput.toString();
    }

    public int getShelfNumber() {
        return shelfNumber;
    }

    public void setShelfNumber(int shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public HashMap<Book, Integer> getBooks() {
        return books;
    }

    public void setBooks(HashMap<Book, Integer> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shelf shelf = (Shelf) o;
        return shelfNumber == shelf.shelfNumber && Objects.equals(subject, shelf.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shelfNumber, subject);
    }

    @Override
    public String toString() {
        return shelfNumber + " : " + subject;
    }
}
