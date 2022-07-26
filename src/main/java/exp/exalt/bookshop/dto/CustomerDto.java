package exp.exalt.bookshop.dto;

import exp.exalt.bookshop.models.Book;
import exp.exalt.bookshop.models.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private long id;
    private String name;
    private List<Book> books;

    public CustomerDto(Customer customer)
    {
        this.id = customer.getId();
        this.name = customer.getName();
        this.setBooks(customer.getBooks());
    }
}
