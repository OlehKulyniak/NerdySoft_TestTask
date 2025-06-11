package cat.project.controller;

import cat.project.exception.MaxBorrowedException;
import cat.project.exception.NotFoundException;
import cat.project.exception.ZeroAmountException;
import cat.project.model.dto.BookDto;
import cat.project.model.dto.BorrowedBookDto;
import cat.project.service.MemberBookService;

import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MemberBookController {
    private final MemberBookService memberBookService;

    @GetMapping("/books/borrowed/members/{name}")
    public ResponseEntity<List<BookDto>> getBorrowedBooksByMemberName(@PathVariable String name) {
        return ResponseEntity.ok(memberBookService.getBorrowedBooksByMemberName(name));
    }

    @GetMapping("/books/borrowed/names")
    public ResponseEntity<List<String>> getDistinctBorrowedBookNames() {
        return ResponseEntity.ok(memberBookService.getDistinctBorrowedBookNames());
    }

    @GetMapping("/books/borrowed/names/count")
    public ResponseEntity<List<BorrowedBookDto>> getDistinctBorrowedBookNamesAndCount() {
        return ResponseEntity.ok(memberBookService.getDistinctBorrowedBookNamesAndCount());
    }

    @PostMapping("/books/borrow/{bookId}/members/{memberId}")
    public ResponseEntity<?> borrowBook(@PathVariable int bookId, @PathVariable int memberId) {
        try {
            memberBookService.borrowBook(memberId, bookId);
            return ResponseEntity.ok().build();
        } catch(MaxBorrowedException | ZeroAmountException | NotFoundException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping("/books/return/{bookId}/members/{memberId}")
    public ResponseEntity<?> returnBook(@PathVariable int bookId, @PathVariable int memberId) {
        try {
            memberBookService.returnBook(memberId, bookId);
            return ResponseEntity.ok().build();
        } catch(NotFoundException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}
