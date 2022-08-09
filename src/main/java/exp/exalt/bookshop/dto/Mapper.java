package exp.exalt.bookshop.dto;

import exp.exalt.bookshop.dto.author_dto.AuthorDto;
import exp.exalt.bookshop.dto.book_dto.BookDto;
import exp.exalt.bookshop.dto.customer_dto.CustomerDto;
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
    public <P,T> T convertForm(P p,Class<T> dest) {
        return modelMapper.map(p,dest);
    }

}
