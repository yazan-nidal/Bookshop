package exp.exalt.bookshop.model;

public class Book {

    private long isbn;
    private String name;
    private long authorId;
    private long customerId;
    private long id;

    public Book(long isbn, String name, long authorId, long customerId, long id) {
        this.isbn = isbn;
        this.name = name;
        this.authorId = authorId;
        this.customerId = customerId;
        this.id = id;
    }

    public Book() {
    }

    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
