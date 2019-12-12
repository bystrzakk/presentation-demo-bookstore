package bstrzkk.test.spring.project.service;

import bstrzkk.test.spring.project.model.Author;
import bstrzkk.test.spring.project.model.Book;
import bstrzkk.test.spring.project.repository.AuthorRepository;
import bstrzkk.test.spring.project.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }
}
