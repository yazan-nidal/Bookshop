package exp.exalt.bookshop.service;

import exp.exalt.bookshop.exception.EmptyEntityException;
import exp.exalt.bookshop.model.Author;
import exp.exalt.bookshop.model.Book;
import exp.exalt.bookshop.model.Customer;
import exp.exalt.bookshop.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AuthorService {

    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    BookService bookService;

    public List<Author> getAuthors() {
        List<Author> authors = new ArrayList<>();
        authorRepository.findAll().forEach(authors::add);
        if(authors.isEmpty()) {
            throw new EmptyEntityException("don't have any Author");
        }
        authors.forEach(author -> author.setName(author.getName().toUpperCase().replaceAll("_+"," ")));
        return authors;
    }

    public Author getAuthorById(long id) {
        Author author = authorRepository.findById(id).orElse(null);
        if(author == null) {
            throw new EntityNotFoundException("Author not Found");
        }
        author.setName(author.getName().toUpperCase().replaceAll("_+"," "));
        return author;
    }

    public Author getAuthorByName(String  name) {
        name = name.toLowerCase().replaceAll(" +","_");
        Author author = authorRepository.findByName(name).orElse(null);
        if(author == null) {
            throw new EntityNotFoundException("Author not Found");
        }
        author.setName(author.getName().toUpperCase().replaceAll("_+"," "));
        return author;
    }

    public Author addAuthor(Author author) {
        if(authorRepository.findById(author.getId()).orElse(null) != null) {
            throw new EntityExistsException("Author 'ID' is used please use another");
        }
        author.setName(author.getName().toLowerCase().replaceAll(" +","_"));
        if(authorRepository.findByName(author.getName()).orElse(null) != null) {
            throw new EntityExistsException("Author 'Name' is used please use another");
        }
            authorRepository.save(author);
            return author;
    }
//
//    public void updateAuthor(Author author, long id) {
//        if(this.getAuthorById(id) != null) {
//            authorRepository.save(author);
//        }
//    }

    public void deleteAuthor(long id) {
        Author author = authorRepository.findById(id).orElse(null);
        if(author == null) {
            throw new EntityNotFoundException("Author not Found");
        }
        author.getBooks().forEach(book -> this.deleteBook(id, book.getIsbn()));
        authorRepository.save(author);
        authorRepository.deleteById(id);
    }

    public List<BookN> getBooks(long id) {
        Author author = authorRepository.findById(id).orElse(null);
        if(author == null) {
            throw new EntityNotFoundException("Author not Found");
        }
        List<Book> books = author.getBooks();
        if(books.isEmpty()) {
            throw new EmptyEntityException("author don't have any Book");
        }
        List<BookN> bookNS= new ArrayList<>();
        books.forEach(book -> bookNS.add(new BookN(book.getName(),
                book.getIsbn(),
                book.getCustomer())));
        return bookNS;
    }

    public BookN getBook(long id, long isbn){
        Author author = authorRepository.findById(id).orElse(null);
        if(author == null) {
            throw new EntityNotFoundException("Author not Found");
        }
        List<Book> books = author.getBooks();
        if(books.isEmpty()) {
            throw new EmptyEntityException("author don't have any Book");
        }
        Book book = books.stream().filter(b -> b.getIsbn() == isbn)
                .findFirst().orElse(null);
        if(book == null) {
            throw new EntityNotFoundException("author don't have this Book");
        }
        return new BookN(book.getName(),
                book.getIsbn(),
                book.getCustomer());
    }

//    public Customer getBookAuthorCustomer(long id, long isbn){
//        return this.getAuthorById(id).getBooks().stream()
//                .filter(b -> b.getIsbn() == isbn).findFirst().orElse(null).getCustomer();
//    }

    public Book addBook(long id, Book book) {
        Author author = authorRepository.findById(id).orElse(null);
        if(author == null) {
            throw new EntityNotFoundException("Author not Found");
        }
        book.setAuthor(author);
        bookService.addBook(book);
        author.getBooks().add(book);
        authorRepository.save(author);
        return book;
    }
//
//    public void updateBook(long id, Book book) {
//        Author author = this.getAuthorById(id);
//        if(author != null) {
//            if(author.getBooks().stream().filter(b -> b.getIsbn()
//                    == book.getIsbn()).findFirst() != null) {
//                author.getBooks().removeIf(b -> b.getIsbn() == book.getIsbn());
//                author.getBooks().add(book);
//                authorRepository.save(author);
//            }
//        }
//    }
//
    public void deleteBook(long id, long isbn) {
        Author author = authorRepository.findById(id).orElse(null);
        if(author == null) {
            throw new EntityNotFoundException("Author not Found");
        }
        author.getBooks().removeIf(b -> b.getIsbn() == isbn);
        authorRepository.save(author);
        bookService.deleteBook(isbn);
    }
}


class BookN{
    public BookN(String name, long isbn, Customer customer) {
        this.name = name;
        this.isbn = isbn;
        this.customer = customer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    String name;
    long isbn;
    Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
};
