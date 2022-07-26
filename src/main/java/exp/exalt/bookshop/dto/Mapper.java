package exp.exalt.bookshop.dto;

import exp.exalt.bookshop.models.Author;
import exp.exalt.bookshop.models.Book;
import exp.exalt.bookshop.models.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    @Autowired
    private ModelMapper modelMapper;

    public BookDto convertToDto(Book book) {
        return modelMapper.map(book, BookDto.class);
    }

    public AuthorDto convertToDto(Author author) {
        return modelMapper.map(author, AuthorDto.class);
    }

    public CustomerDto convertToDto(Customer customer) {
        return modelMapper.map(customer, CustomerDto.class);
    }

    public Book convertToEntity(BookDto bookDto) {
        return modelMapper.map(bookDto, Book.class);
    }

    public Book bookDtoToBook(BookDtoC bookDtoC) {
        return modelMapper.map(bookDtoC, Book.class);
    }

    public Author convertToEntity(AuthorDto authorDto) {
        return modelMapper.map(authorDto, Author.class);
    }

    public Customer convertToEntity(CustomerDto customerDto) {
        return modelMapper.map(customerDto, Customer.class);
    }

    public Author userToAuthor(UserDto userDto) {
        return modelMapper.map(userDto, Author.class);
    }

    public Customer userToCustomer(UserDto userDto) {
        return modelMapper.map(userDto, Customer.class);
    }




}
