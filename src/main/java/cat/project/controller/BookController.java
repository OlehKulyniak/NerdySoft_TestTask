package cat.project.controller;

import cat.project.exception.BookBorrowedException;
import cat.project.exception.NotFoundException;
import cat.project.model.dto.BookDto;
import cat.project.service.BookService;

import org.springframework.http.ResponseEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable int id) {
        try {
            BookDto bookDto = bookService.getBookById(id);
            return ResponseEntity.ok(bookDto);
        }
        catch(NotFoundException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }

    }

    @PostMapping
    public ResponseEntity<BookDto> saveBook(@RequestBody BookDto bookDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(bookService.saveBook(bookDto));
    }

    @PutMapping
    public ResponseEntity<?> updateBook(@RequestBody BookDto bookDto) {
        try {
            return ResponseEntity.ok(bookService.updateBook(bookDto));
        }
        catch(NotFoundException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable int id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.ok().build();
        } catch(BookBorrowedException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}
