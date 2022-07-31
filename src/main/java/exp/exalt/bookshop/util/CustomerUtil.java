package exp.exalt.bookshop.util;

import exp.exalt.bookshop.dto.book_dto.BookDto;
import exp.exalt.bookshop.dto.customer_dto.CustomerBookDto;
import exp.exalt.bookshop.dto.customer_dto.CustomerDto;
import exp.exalt.bookshop.dto.Mapper;
import exp.exalt.bookshop.exceptions.author_exceptions.*;
import exp.exalt.bookshop.exceptions.book_exceptions.BookExistsException;
import exp.exalt.bookshop.models.Book;
import exp.exalt.bookshop.models.Customer;
import exp.exalt.bookshop.services.CustomerService;
import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static exp.exalt.bookshop.constants.ConstVar.*;
import static exp.exalt.bookshop.constants.ConstVar.MODEL_MAPPER_ILLEGAL_EXCEPTION;

@Service
public class CustomerUtil {
    @Autowired
    CustomerService customerService;
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

    public CustomerDto addCustomer(CustomerDto customer) {
        CustomerDto customerDto;
        Customer customerI= convertNewForm(customer, Customer.class);
        List<Book> books = customerI.getBooks();
        customerI.setBooks(new ArrayList<>());
        customerDto = convertNewForm(customerService.addCustomerOrUpdate(customerI),CustomerDto.class);
        if(books != null
                && !books.isEmpty()) {
            customerDto = convertNewForm(addCustomerBooks(customerI,books),CustomerDto.class);
        }
        return customerDto;
    }

    private Customer addCustomerBooks(Customer customer, List<Book> books){

            for(Book book: books) {
                book.setCustomer(customer);
                if((book.getIsbn() == 0)
                        || (customer.getBookByIsbn(book.getIsbn()) != null)) {
                    throw new BookExistsException(BOOK_EXISTS);
                }
            }
            customer.addBooks(books);
            customerService.addCustomerOrUpdate(customer);
            customer = customerService.getCustomerById(customer.getId());
        return customer;
    }

    public CustomerDto updateCustomer(long id,CustomerDto customer) {
        CustomerDto customerDto;
        Customer customerI = customerService.getCustomerById(id);
            customer.setId(id);
            customerDto = updateCustomerWithOptionalBooks(customer,customerI);
        return customerDto;
    }

    private CustomerDto updateCustomerWithOptionalBooks(CustomerDto customerDto, Customer customer)
    {
            Customer customerI= convertNewForm(customerDto, Customer.class);
            if(customerDto.getBooks() != null ) {
                if(!customerDto.getBooks().isEmpty()) {
                    if(customer.getBooks() != null
                            && !customer.getBooks().isEmpty()) {
                        this.deleteCustomerBooks(customer.getId());
                    }
                }
            } else {
                customerI.setBooks(customer.getBooks());
                customerService.addCustomerOrUpdate(customerI);
                return convertNewForm(customerI,CustomerDto.class);
            }
            List<Book> books = new ArrayList<>(customerI.getBooks());
            customerI.setBooks(new ArrayList<>());
            customerDto = convertNewForm(this.addCustomerBooks(customerI, books),CustomerDto.class);
        return customerDto;
    }

    public List<CustomerBookDto> deleteCustomerBooks(long id) {
        Customer customer;
        List<CustomerBookDto> bookDtoList;
           customer = customerService.getCustomerById(id);
           bookDtoList = mapListForm(deleteCustomerBooks(customer), CustomerBookDto.class);
        return bookDtoList;
    }

    private List<BookDto> deleteCustomerBooks(Customer customer) {
        List<BookDto> bookDtoList;
            List<Book> books = new ArrayList<>(customer.getBooks());
            customer.removeAllBooks();
            customerService.addCustomerOrUpdate(customer);
            bookDtoList = mapListForm(books,BookDto.class);
        return bookDtoList;
    }

    public CustomerDto deleteCustomer(long id){
        CustomerDto customerDto;
            Customer customer = customerService.getCustomerById(id);
            customerService.deleteCustomer(id);
            customerDto = convertNewForm(customer, CustomerDto.class);
        return customerDto;
    }

    public CustomerDto rentCustomerBook(long id, BookDto bookDto) throws AuthorGeneralException {
        CustomerDto customerDto;
            Customer customer = customerService.getCustomerById(id);
            customerDto = convertNewForm(rentCustomerBook(customer,bookDto),CustomerDto.class);
        return customerDto;
    }

    private Customer rentCustomerBook(Customer customer, BookDto bookDto){
            Book book = convertNewForm(bookDto,Book.class);
            book.setCustomer(customer);
            customer.addBook(book);
            customer = customerService.addCustomerOrUpdate(customer);
        return customer;
    }

    public CustomerDto rentCustomerBooks(long id, List<BookDto> bookDtoList) throws AuthorGeneralException {
        CustomerDto customerDto;
           Customer customer = customerService.getCustomerById(id);
            List<Book> books = mapListForm(bookDtoList,Book.class);
            customerDto = convertNewForm(rentCustomerBooks(customer,books),CustomerDto.class);
        return customerDto;
    }

    private Customer rentCustomerBooks(Customer customer, List<Book> books){
            for(Book book: books) {
                book.setCustomer(customer);
                if((book.getIsbn() == 0)
                        || (customer.getBookByIsbn(book.getIsbn()) != null)) {
                    throw new BookExistsException(BOOK_EXISTS);
                }
            }
            customer.addBooks(books);
            customerService.addCustomerOrUpdate(customer);
            customer = customerService.getCustomerById(customer.getId());
        return customer;
    }


    public CustomerBookDto returnCustomerBook(long id, long isbn){
        CustomerBookDto bookDto;
            Customer customer = customerService.getCustomerById(id);
            bookDto = convertNewForm(returnCustomerBook(customer,isbn),CustomerBookDto.class);
        return bookDto;
    }

    private BookDto returnCustomerBook(Customer customer, long isbn) {
        BookDto bookDto;
            Book book = customer.getBookByIsbn(isbn);
            // Removes all the elements of this collection that satisfy the given predicate.
            // but isbn is unique so it's remove one.
            customer.removeBooks(b -> b.getIsbn() == isbn);
            customerService.addCustomerOrUpdate(customer);
            bookDto = convertNewForm(book,BookDto.class);
        return bookDto;
    }

    public List<CustomerBookDto> returnCustomerBooks(long id){
        Customer customer;
        List<CustomerBookDto> bookDtoList;
            customer =customerService.getCustomerById(id);
            bookDtoList = mapListForm(returnCustomerBooks(customer), CustomerBookDto.class);
        return bookDtoList;
    }

    private List<BookDto> returnCustomerBooks(Customer customer) {
        List<BookDto> bookDtoList;
            List<Book> books = new ArrayList<>(customer.getBooks());
            customer.removeAllBooks();
            customerService.addCustomerOrUpdate(customer);
            bookDtoList = mapListForm(books,BookDto.class);
        return bookDtoList;
    }

    public CustomerBookDto getCustomerBookByIsbn(long id, long isbn) throws AuthorGeneralException {
        CustomerBookDto bookDto;
            Customer customer = customerService.getCustomerById(id);
            Book book = customer.getBookByIsbn(isbn);
            bookDto = convertNewForm(book,CustomerBookDto.class);
        return bookDto;
    }

    public List<CustomerBookDto> getCustomerBooksByName(long id, String name) throws AuthorGeneralException {
        List<CustomerBookDto> bookDtoList;
            Customer customer = customerService.getCustomerById(id);
            List<Book> books =  customer.getBooks(book -> book.getName().equals(name));
            bookDtoList = mapListForm(books,CustomerBookDto.class);
        return bookDtoList;
    }

    // map methods
    private <P,T> List<T> mapListForm(List<P> p, Class<T> dest) {
        List<T>  t;
        try {
            t = p.stream()
                    .map(pi-> mapper.convertForm(pi,dest))
                    .collect(Collectors.toList());
        } catch (ConfigurationException
                 | IllegalArgumentException
                 | MappingException ex) {
            throw new AuthorGeneralException(MODEL_MAPPER_ILLEGAL_EXCEPTION);
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
            throw new AuthorGeneralException(MODEL_MAPPER_ILLEGAL_EXCEPTION);
        }
        return t;
    }
}
