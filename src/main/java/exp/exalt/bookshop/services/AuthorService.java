package exp.exalt.bookshop.services;

import exp.exalt.bookshop.models.Author;
import exp.exalt.bookshop.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorService {
    @Autowired
    AuthorRepository authorRepository;

    public List<Author> getAuthors() {
        List<Author> authors = new ArrayList<>();
        authorRepository.findAll().forEach(authors::add);
        return authors;
    }

    public Author getAuthorById(long id) {
        return authorRepository.findById(id).orElse(null);
    }

    public List<Author> getAuthorsByName(String name) {
        List<Author> authors = new ArrayList<>();
        authorRepository.findAllByName(name).forEach(authors::add);
        return authors;
    }

    public Author addAuthor(Author author) {
        authorRepository.save(author);
        return author;
    }

    public void deleteAuthor(long id) {
        authorRepository.deleteById(id);
    }
}
