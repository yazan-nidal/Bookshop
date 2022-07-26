package exp.exalt.bookshop.util;

import exp.exalt.bookshop.dto.AuthorDto;
import exp.exalt.bookshop.dto.Mapper;
import exp.exalt.bookshop.exceptions.author_exceptions.AuthorExistsException;
import exp.exalt.bookshop.exceptions.author_exceptions.EmptyAuthorListException;
import exp.exalt.bookshop.exceptions.author_exceptions.AuthorNotFoundException;
import exp.exalt.bookshop.models.Author;
import exp.exalt.bookshop.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static exp.exalt.bookshop.constants.ConstVar.AUTHOR_CONFLICT;
import static exp.exalt.bookshop.constants.ConstVar.AUTHOR_NOT_FOUND;

@Service
public class AuthorUtil {
    @Autowired
    AuthorService authorService;
    @Autowired
    Mapper mapper;

    public List<AuthorDto> getAuthors() {
        List<Author> authors = authorService.getAuthors();
        if(authors.isEmpty()) {
            throw new EmptyAuthorListException();
        }
        return authors.stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    public AuthorDto getAuthorById(long id) {
        Author author = authorService.getAuthorById(id);
        if(author == null){
            throw new AuthorNotFoundException(AUTHOR_NOT_FOUND);
        }
        return mapper.convertToDto(author);
    }

    public List<AuthorDto> getAuthorsByName(String name) {
        List<Author> authors = authorService.getAuthorsByName(name);
        if(authors.isEmpty()) {
            throw new AuthorNotFoundException(AUTHOR_NOT_FOUND);
        }
        return authors.stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    public AuthorDto addAuthor(AuthorDto author) {
        if(authorService.getAuthorById(author.getId()) != null) {
            throw new AuthorExistsException(AUTHOR_CONFLICT);
        }
        authorService.addAuthor(mapper.convertToEntity(author));
        return author;
    }

    public void deleteAuthor(long id) {
        Author author = authorService.getAuthorById(id);
        if(author == null){
            throw new AuthorNotFoundException(AUTHOR_NOT_FOUND);
        }
        authorService.deleteAuthor(id);
    }
}
