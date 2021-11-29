import java.util.ArrayList;
import java.util.List;
import java.util.*;

/**
 * title: Reader.java
 * abstract: This class Reader is used to create reader objects that will be part of the Library Project.  The
 * reader can check out and return Books from the Library.  The Library Project has 4 classes: Library, Shelf,
 * Book, and Reader.  These classes will be used to store, manipulate, and analyse information stored in the library.
 * name: Juli S
 * date: 11/06/2021
 */

public class Reader {
    // Constants used as index
    public static final int CARD_NUMBER_ = 0;
    public static final int NAME_ = 1;
    public static final int PHONE_ = 2;
    public static final int BOOK_COUNT_ = 3; // used to process books the reader has checked out
    public static final int BOOK_START_ = 4; // the beginning of a listing of books the reader has checked out

    // Variables
    private int cardNumber;
    private String name;
    private String phone;
    private List<Book> books;

    public Reader(int cardNumber, String name, String phone) {
        this.cardNumber = cardNumber;
        this.name = name;
        this.phone = phone;
        books = new ArrayList<>();
    }

    /**
     * This addBook() method adds a book into the reader's list if it is not already there.  Duplicates are not allowed.
     * @param book an object representing a book on the shelf
     * @return code indicating if this method was successful or not.
     */
    public Code addBook(Book book) {
        if(hasBook(book)) {
            return Code.BOOK_ALREADY_CHECKED_OUT_ERROR;
        } else {
            this.books.add(book);
            return Code.SUCCESS;
        }
    }

    /**
     * This removeBook() method takes a book out of the reader's book list if it is there.  This method cannot
     * remove a book that is not in the reader's list.
     * @param book an object representing a book in the readers list of books
     * @return code indicating if this method was successful or not.
     */
    public Code removeBook(Book book) {
        if(hasBook(book)) {
            this.books.remove(book);
            return Code.SUCCESS;
        } else {
            return Code.READER_DOESNT_HAVE_BOOK_ERROR;
        }
    }

    /**
     * This hasBook() method checks if the book is in the reader's list.
     * @param book an object representing a book the reader is interacting with.
     * @return True if book is in reader's book list or false if it is not in reader's book list.
     */
    public boolean hasBook(Book book) {
        return this.books.contains(book);
    }

    /**
     * This getBookCount() method figures out how many books are in the reader's list of books.
     * @return number of books in reader's book list.
     */
    public int getBookCount() {
        return this.books.size();
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reader reader = (Reader) o;
        return cardNumber == reader.cardNumber && Objects.equals(name, reader.name) && Objects.equals(phone, reader.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, name, phone);
    }

    /**
     * This toString() method displays a person's checkout receipt
     * @return [name] (#[cardNumber]) has checked out {[books]}
     */
    @Override
    public String toString() {
        return name + " (#" + cardNumber + ") has the following books: " + books;
    }
}
