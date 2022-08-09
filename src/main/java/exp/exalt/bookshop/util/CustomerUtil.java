package exp.exalt.bookshop.util;

import exp.exalt.bookshop.dto.book_dto.BookDto;
import exp.exalt.bookshop.dto.customer_dto.CustomerDto;
import exp.exalt.bookshop.dto.Mapper;
import exp.exalt.bookshop.exceptions.GeneralException;
import exp.exalt.bookshop.exceptions.author_exceptions.*;
import exp.exalt.bookshop.models.Book;
import exp.exalt.bookshop.models.Customer;
import exp.exalt.bookshop.models.RoleID;
import exp.exalt.bookshop.services.BookService;
import exp.exalt.bookshop.services.CustomerService;
import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static exp.exalt.bookshop.constants.ConstVar.MODEL_MAPPER_ILLEGAL_EXCEPTION;

@Service
public class CustomerUtil {
    @Autowired
    CustomerService customerService;
    @Autowired
    BookService bookService;
    @Autowired
    Mapper mapper;

    public List<CustomerDto> getCustomers() {
        List<Customer> customers;
        List<CustomerDto> customerDtoList;
        customers = getALLCustomerFromAuthorService();
        customerDtoList = mapListForm(customers , CustomerDto.class);
        return customerDtoList;
    }

    private List<Customer> getALLCustomerFromAuthorService(){
        Iterable<Customer> customerIterable;
        List<Customer> customers;
        customerIterable = customerService.getCustomers();
        customers = new ArrayList<>();
        customerIterable.forEach(customers::add);
        return customers;
    }

    public CustomerDto getCustomerById(long id) {
        Customer customer;
        CustomerDto customerDto;
        customer = customerService.getCustomerById(id);
        customerDto = convertNewForm(customer,CustomerDto.class);
        return customerDto;
    }

    public List<CustomerDto> getCustomersByName(String name) {
        List<Customer> customers;
        List<CustomerDto> customerDtoList;
        customers = customerService.getCustomersByName(name);
        customerDtoList = mapListForm(customers,CustomerDto.class);
        return customerDtoList;
    }

    public CustomerDto getCustomerByUsername(String username) {
        Customer customer;
        CustomerDto customerDto;
        customer = customerService.getCustomerByUsername(username);
        customerDto = convertNewForm(customer,CustomerDto.class);
        return customerDto;
    }

    public CustomerDto addCustomer(CustomerDto customer) {
        CustomerDto customerDto = null;
        customer.setRole(RoleID.CUSTOMER.getRole());
        Customer customerI= convertNewForm(customer, Customer.class);
        List<Book> books = customerI.getBooks();
        customerI.setBooks(new ArrayList<>());
        customerI =customerService.addCustomerOrUpdate(customerI);
        customerDto = convertNewForm(customerI,CustomerDto.class);
        if(books != null
                && !books.isEmpty()) {
            customerI = addCustomerBooks(customerI,books);
            //mapping customer with book have authored
            customerDto = convertNewForm(customerI,CustomerDto.class);
        }
        customerDto.setPassword(null);
        return customerDto;
    }

    private Customer addCustomerBooks(Customer customer, List<Book> books){
        List<Book> bookList =  new ArrayList<>();
            for(Book book: books) {
                if(bookService.getBookByIsbn(book.getIsbn()) != null) {
                    Book bookI = bookService.getBookByIsbn(book.getIsbn());
                    if(bookI.getCustomer() == null) {
                        bookI.setCustomer(customer);
                        bookList.add(bookI);
                    }
                }
            }
            customer.addBooks(bookList);
            customerService.addCustomerOrUpdate(customer);
            customer = customerService.getCustomerById(customer.getId());
        return customer;
    }

    public CustomerDto deleteCustomer(long id){
        CustomerDto customerDto;
        Customer customer = customerService.getCustomerById(id);
        this.returnCustomerBooks(customer);
        customerService.deleteCustomer(id);
        customerDto = convertNewForm(customer, CustomerDto.class);
        customerDto.setPassword(null);
        return customerDto;
    }

    public CustomerDto deleteCustomer(String username){
        CustomerDto customerDto;
        Customer customer = customerService.getCustomerByUsername(username);
        this.returnCustomerBooks(customer);
        customerService.deleteCustomer(username);
        customerDto = convertNewForm(customer, CustomerDto.class);
        customerDto.setPassword(null);
        return customerDto;
    }

    public CustomerDto rentCustomerBookByIsbn(long id, long isbn) throws GeneralException {
        CustomerDto customerDto = null;
            Customer customer = customerService.getCustomerById(id);
            Book book = bookService.getBookByIsbn(isbn);
            if(book != null) {
                customerDto = convertNewForm(rentCustomerBook(customer,book), CustomerDto.class);
            }
        customerDto.setPassword(null);
        return customerDto;
    }

    public CustomerDto rentCustomerBookById(long id, long book_id) throws GeneralException {
        CustomerDto customerDto = null;
        Customer customer = customerService.getCustomerById(id);
        Book book = bookService.getBookById(book_id);
        if(book != null) {
            customerDto = convertNewForm(rentCustomerBook(customer,book), CustomerDto.class);
        }
        customerDto.setPassword(null);
        return customerDto;
    }

    public CustomerDto rentCustomerBookByIsbn(String username, long isbn) throws GeneralException {
        CustomerDto customerDto = null;
        Customer customer = customerService.getCustomerByUsername(username);
        Book book = bookService.getBookByIsbn(isbn);
        if(book != null) {
            customerDto = convertNewForm(rentCustomerBook(customer,book), CustomerDto.class);
        }
        customerDto.setPassword(null);
        return customerDto;
    }

    public CustomerDto rentCustomerBookById(String username, long book_id) throws GeneralException {
        CustomerDto customerDto = null;
        Customer customer = customerService.getCustomerByUsername(username);
        Book book = bookService.getBookById(book_id);
        if(book != null) {
            customerDto = convertNewForm(rentCustomerBook(customer,book), CustomerDto.class);
        }
        customerDto.setPassword(null);
        return customerDto;
    }

    private Customer rentCustomerBook(Customer customer, Book book) {
            if(book.getCustomer() == null) {
            book.setCustomer(customer);
            customer.addBook(book);
            }
            customer = customerService.addCustomerOrUpdate(customer);
        return customer;
    }

    public BookDto returnCustomerBookByIsbn(long id, long isbn){
        BookDto bookDto;
        Customer customer = customerService.getCustomerById(id);
        Book book = customer.getBookByIsbn(isbn);
        bookDto = convertNewForm(returnCustomerBook(customer,book,b -> b.getIsbn() == isbn),BookDto.class);
        return bookDto;
    }

    public BookDto returnCustomerBookByIsbn(String username, long isbn){
        BookDto bookDto;
        Customer customer = customerService.getCustomerByUsername(username);
        Book book = customer.getBookByIsbn(isbn);
        bookDto = convertNewForm(returnCustomerBook(customer,book,b -> b.getIsbn() == isbn),BookDto.class);
        return bookDto;
    }

    public BookDto returnCustomerBookById(long id, long book_id){
        BookDto bookDto;
        Customer customer = customerService.getCustomerById(id);
        Book book = customer.getBookById(book_id);
        bookDto = convertNewForm(returnCustomerBook(customer,book, b -> b.getId() == book_id),BookDto.class);
        return bookDto;
    }

    public BookDto returnCustomerBookById(String username, long book_id){
        BookDto bookDto;
        Customer customer = customerService.getCustomerByUsername(username);
        Book book = customer.getBookById(book_id);
        bookDto = convertNewForm(returnCustomerBook(customer,book, b -> b.getId() == book_id),BookDto.class);
        return bookDto;
    }

    private BookDto returnCustomerBook(Customer customer, Book book, Predicate<? super Book> filter) {
        BookDto bookDto;
        book.setCustomer(null);
        // Removes all the elements of this collection that satisfy the given predicate.
        // but isbn is unique so it's remove one.
        customer.removeBooks(filter);
        customerService.addCustomerOrUpdate(customer);
        bookDto = convertNewForm(book,BookDto.class);
        return bookDto;
    }

    public List<BookDto> returnCustomerBooks(long id) {
        Customer customer;
        List<BookDto> bookDtoList;
           customer = customerService.getCustomerById(id);
           bookDtoList = mapListForm(returnCustomerBooks(customer), BookDto.class);
        return bookDtoList;
    }

    public List<BookDto> returnCustomerBooks(String username) {
        Customer customer;
        List<BookDto> bookDtoList;
        customer = customerService.getCustomerByUsername(username);
        bookDtoList = mapListForm(returnCustomerBooks(customer), BookDto.class);
        return bookDtoList;
    }

    private List<BookDto> returnCustomerBooks(Customer customer) {
        List<BookDto> bookDtoList;
        List<Book> books = new ArrayList<>(customer.getBooks());
        for (Book book :books) {
            book.setCustomer(null);
        }
        customer.removeAllBooks();
        customerService.addCustomerOrUpdate(customer);
        bookDtoList = mapListForm(books,BookDto.class);
        return bookDtoList;
    }

    public CustomerDto updateCustomer(long id,CustomerDto customer) {
        CustomerDto customerDto;
        Customer customerI = customerService.getCustomerById(id);
        customer.setId(customerI.getId());
        customer.setRole(RoleID.CUSTOMER.getRole());
        customerDto = updateCustomerWithOptionalBooks(customer,customerI);
        customerDto.setPassword(null);
        return customerDto;
    }

    public CustomerDto updateCustomer(String username,CustomerDto customer) {
        CustomerDto customerDto;
        Customer customerI = customerService.getCustomerByUsername(username);
        customer.setId(customerI.getId());
        customer.setRole(RoleID.CUSTOMER.getRole());
        customerDto = updateCustomerWithOptionalBooks(customer,customerI);
        customerDto.setPassword(null);
        return customerDto;
    }

    private CustomerDto updateCustomerWithOptionalBooks(CustomerDto customerDto, Customer customer)
    {
            Customer customerI= convertNewForm(customerDto, Customer.class);
            String  newUsername = customerI.getUsername();
            if(customerDto.getBooks() != null ) {
                if(!customerDto.getBooks().isEmpty()) {
                    if(customer.getBooks() != null
                            && !customer.getBooks().isEmpty()) {
                        //this.returnCustomerBooks(customer.getId());
                    }
                }
            } else {
                customerI.setBooks(customer.getBooks());
                customerService.addCustomerOrUpdate(customerI);
                return convertNewForm(customerI,CustomerDto.class);
            }
            List<Book> books = new ArrayList<>(customerI.getBooks());
            customerI.setBooks(customer.getBooks());
            customerService.addCustomerOrUpdate(customerI);
            customerI = this.addCustomerBooks(customerI, books);
            customerDto = convertNewForm(customerI,CustomerDto.class);
        return customerDto;
    }

    public BookDto getCustomerBookByIsbn(long id, long isbn) throws GeneralException {
        BookDto bookDto;
            Customer customer = customerService.getCustomerById(id);
            Book book = customer.getBookByIsbn(isbn);
            bookDto = convertNewForm(book,BookDto.class);
        return bookDto;
    }

    public BookDto getCustomerBookByIsbn(String username, long isbn) throws GeneralException {
        BookDto bookDto;
        Customer customer = customerService.getCustomerByUsername(username);
        Book book = customer.getBookByIsbn(isbn);
        bookDto = convertNewForm(book,BookDto.class);
        return bookDto;
    }

    public BookDto getCustomerBookById(long id, long book_id) throws GeneralException {
        BookDto bookDto;
        Customer customer = customerService.getCustomerById(id);
        Book book = customer.getBookById(book_id);
        bookDto = convertNewForm(book,BookDto.class);
        return bookDto;
    }

    public BookDto getCustomerBookById(String username, long book_id) throws GeneralException {
        BookDto bookDto;
        Customer customer = customerService.getCustomerByUsername(username);
        Book book = customer.getBookById(book_id);
        bookDto = convertNewForm(book,BookDto.class);
        return bookDto;
    }

    public List<BookDto> getCustomerBooksByName(long id, String name) throws GeneralException {
        List<BookDto> bookDtoList;
            Customer customer = customerService.getCustomerById(id);
            List<Book> books =  customer.getBooks(book -> book.getName().equals(name));
            bookDtoList = mapListForm(books,BookDto.class);
        return bookDtoList;
    }

    public List<BookDto> getCustomerBooksByName(String username, String name) throws GeneralException {
        List<BookDto> bookDtoList;
        Customer customer = customerService.getCustomerByUsername(username);
        List<Book> books =  customer.getBooks(book -> book.getName().equals(name));
        bookDtoList = mapListForm(books,BookDto.class);
        return bookDtoList;
    }

    public List<BookDto> getCustomerBooks(long id){
        List<BookDto> bookDtoList;
            Customer customer = customerService.getCustomerById(id);
            List<Book> books = customer.getBooks();
            bookDtoList = mapListForm(books,BookDto.class);
        return bookDtoList;
    }

    public List<BookDto> getCustomerBooks(String username){
        List<BookDto> bookDtoList;
        Customer customer = customerService.getCustomerByUsername(username);
        List<Book> books = customer.getBooks();
        bookDtoList = mapListForm(books,BookDto.class);
        return bookDtoList;
    }

//    // map methods
    private <P,T> List<T> mapListForm(List<P> p, Class<T> dest) {
        List<T>  t;
        try {
            t = p.stream()
                    .map(pi-> mapper.convertForm(pi,dest))
                    .collect(Collectors.toList());
        } catch (ConfigurationException
                 | IllegalArgumentException
                 | MappingException ex) {
            throw new GeneralException(MODEL_MAPPER_ILLEGAL_EXCEPTION);
        }
        return t;
    }

    private <P,T> T convertNewForm(P p,Class<T> dest) {
        T t;
        try {
            t = mapper.convertForm(p,dest);
        } catch (ConfigurationException
                 | IllegalArgumentException
                 | MappingException ex) {
            throw new GeneralException(MODEL_MAPPER_ILLEGAL_EXCEPTION);
        }
        return t;
    }
}
