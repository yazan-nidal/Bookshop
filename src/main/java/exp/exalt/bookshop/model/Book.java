package exp.exalt.bookshop.model;

public class Book {

    private long bookId;
    private String title;
    private long authorId;

    public Book(long bookId, String title, long authorId) {
        this.bookId = bookId;
        this.title = title;
        this.authorId = authorId;
    }

    public Book() {
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }
}
