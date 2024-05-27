package ru.chaplyginma.samplereactivewebapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.chaplyginma.samplereactivewebapp.model.Book;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/books")
@Slf4j
public class SampleController {
    private final Map<Integer, Book> books = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger();

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<Book> getAllBooks() {
        return Flux.fromIterable(books.values());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Book> getBookById(
            @PathVariable int id
    ) {
        Book book = books.get(id);
        if (book != null) {
            return Mono.just(book);
        } else {
            return Mono.empty();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Book> addBook(
            @RequestBody Book book
    ) {
        int id = idGenerator.incrementAndGet();
        book.setId(id);
        books.put(id, book);
        return Mono.just(book);
    }
}
