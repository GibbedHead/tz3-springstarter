package ru.chaplyginma.samplewebapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.chaplyginma.samplewebapp.model.Book;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/books")
public class SampleController {
    private final Map<Integer, Book> books = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger();

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Book> getAllBooks() {
        return books.values();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Book getBookById(
            @PathVariable int id
    ) {
        return books.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book addBook(
            @RequestBody Book book
    ) {
        int id = idGenerator.incrementAndGet();
        book.setId(id);
        books.put(id, book);
        return book;
    }
}
