package exp.exalt.bookshop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
public class Book {

    @NotNull
    @Column(unique=true)
    private long isbn;
    @NotNull
    @Column(unique=true)
    private String name;
    @NotNull
    @JsonManagedReference
    @ManyToOne
    private Author author;
    @ManyToOne
    private Customer customer;
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    public Book(long isbn, String name, Author author, Customer customer) {
        this.isbn = isbn;
        this.name = name;
        this.author = author;
        this.customer = customer;
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

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
