package exp.exalt.bookshop.service;

import exp.exalt.bookshop.exception.EmptyEntityException;
import exp.exalt.bookshop.model.Author;
import exp.exalt.bookshop.model.Book;
import exp.exalt.bookshop.model.Customer;
import exp.exalt.bookshop.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        bookRepository.findAll().forEach(books::add);
        if(books.isEmpty()) {
            throw new EmptyEntityException("don't have any Book");
        }
        books.forEach(book -> book.setName(book.getName().toUpperCase().replaceAll("_+"," ")));
        return books;
    }

    public Book getBookByIsbn(long isbn) {
        Book book = bookRepository.findByIsbn(isbn).orElse(null);
        if(book== null) {
            throw new EntityNotFoundException("Book not Found");
        }
        book.setName(book.getName().toUpperCase().replaceAll("_+"," "));
        return book;
    }

    public Book getBookByName(String  name) {
        name = name.toLowerCase().replaceAll(" +","_");
        Book book = bookRepository.findByName(name).orElse(null);
        if(book== null) {
            throw new EntityNotFoundException("Book not Found");
        }
        book.setName(book.getName().toUpperCase().replaceAll("_+"," "));
        return book;
    }

    @Transactional
    public Book addBook(Book book) {
        System.out.println("------------------------------------------------------------------------------");
        if(bookRepository.findByIsbn(book.getIsbn()).orElse(null) != null) {
            throw new EntityExistsException("Book 'ISBN' is used please use another");
        }
        book.setName(book.getName().toLowerCase().replaceAll(" +","_"));
        if(bookRepository.findByName(book.getName()).orElse(null) != null) {
            throw new EntityExistsException("Book 'Name' is used please use another");
        }
        System.out.println("------------------------------------------------------------------------------");
        bookRepository.save(book);
        return book;
    }

//    public void updateBook(Book book, long isbn){
//        Book bookT = this.getBook(isbn);
//        if(bookT != null) {
//            book.setId(bookT.getId());
//            if(book.getIsbn() <= 0) {
//                book.setIsbn(bookT.getIsbn());
//            }
//            bookRepository.save(book);
//        }
//    }
//
    public void deleteBook(long isbn) {
        Book book = bookRepository.findByIsbn(isbn).orElse(null);
        if(book== null) {
            throw new EntityNotFoundException("Book not Found");
        }
        bookRepository.deleteByIsbn(isbn);
    }
//
//    public Author getBookAuthor(long id) {
//        Book book = this.getBook(id);
//        return (book == null)? null : book.getAuthor();
//    }
//
//    public Customer getBookCustomer(long id) {
//        Book book = this.getBook(id);
//        return (book == null)? null : book.getCustomer();
//    }
//
//    public void updateBookCustomer(Customer customer, long id) {
//        Book book = this.getBook(id);
//        if(book != null) {
//            book.setCustomer(customer);
//            bookRepository.save(book);
//        }
//    }
}
