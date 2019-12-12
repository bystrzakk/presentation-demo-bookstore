package bstrzkk.test.spring.project.controller;

import bstrzkk.test.spring.project.model.Author;
import bstrzkk.test.spring.project.model.Book;
import bstrzkk.test.spring.project.model.response.User;
import bstrzkk.test.spring.project.service.BookService;
import bstrzkk.test.spring.project.service.RegresClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final RegresClient regresClient;

    @GetMapping(path = "/books")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping(path = "/authors")
    public List<Author> getAllAuthors() {
        return bookService.getAllAuthors();
    }

    @GetMapping(path = "/users/{id}")
    public User getUser(@PathVariable("id") Integer id) {
        return regresClient.fetchUser(id);
    }

    @GetMapping(path = "/users")
    public List<User> getUsers() {
        return regresClient.fetchAllUsers();
    }

    @PostMapping(value = "/admin/book", produces = APPLICATION_JSON_VALUE)
    public Book addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }
}
