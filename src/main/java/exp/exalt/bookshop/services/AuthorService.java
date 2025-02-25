package exp.exalt.bookshop.services;

import exp.exalt.bookshop.models.Author;
import exp.exalt.bookshop.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthorService {
    @Autowired
    AuthorRepository authorRepository;

    @Transactional
    public Iterable<Author> getAuthors() {
        return authorRepository.findAll();
    }

    @Transactional
    public Author getAuthorById(long id) {
        return authorRepository.findById(id).orElse(null);
    }

    @Transactional
    public Author getAuthorByUsername(String username) {
        return authorRepository.findByUsername(username).orElse(null);
    }

    @Transactional
    public List<Author> getAuthorsByName(String name) {
        return authorRepository.findAllByName(name);
    }

    @Transactional
    public Author addAuthorOrUpdate(Author author) {
       return authorRepository.save(author);
    }

    @Transactional
    public void deleteAuthor(long id) {
        authorRepository.deleteById(id);
    }

    @Transactional
    public void deleteAuthor(String username) {
        authorRepository.deleteByUsername(username);
    }

}
