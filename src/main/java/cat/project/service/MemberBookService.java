package cat.project.service;

import cat.project.exception.MaxBorrowedException;
import cat.project.exception.NotFoundException;
import cat.project.exception.ZeroAmountException;
import cat.project.mapper.BookMapper;
import cat.project.model.dto.BookDto;
import cat.project.model.dto.BorrowedBookDto;
import cat.project.model.entity.Book;
import cat.project.model.entity.Member;
import cat.project.model.entity.MemberBook;
import cat.project.repository.BookRepository;
import cat.project.repository.MemberBookRepository;
import cat.project.repository.MemberRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberBookService {
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final MemberBookRepository memberBookRepository;
    private final BookMapper bookMapper;

    @Value("maxBorrowedBooks")
    private String maxBorrowedBooks;

    public List<BookDto> getBorrowedBooksByMemberName(String memberName) {
        List<Book> books = memberBookRepository.findAllBookByMember_Name(memberName);
        List<BookDto> bookDtos = new ArrayList<>();
        for(var book : books) {
            bookDtos.add(bookMapper.bookToDto(book));
        }
        return bookDtos;
    }

    public List<String> getDistinctBorrowedBookNames() {
        return memberBookRepository.findAllDistinctBook_Title();
    }

    public List<BorrowedBookDto> getDistinctBorrowedBookNamesAndCount() {
        return memberBookRepository.findAllDistinctBook_TitleAndCount();
    }

    @Transactional
    public void borrowBook(int memberId, int bookId) throws NotFoundException, ZeroAmountException, MaxBorrowedException {
        Member member = memberRepository.findById(memberId);
        if(member == null) {
            throw new NotFoundException("Member with id - " + memberId + " was not found.");
        }
        if(member.getBorrowedBooks().size() < Integer.parseInt(maxBorrowedBooks)) {
            Book book = bookRepository.findById(bookId);
            if(book == null) {
                throw new NotFoundException("Book with id - " + bookId + " was not found.");
            }
            if(book.getAmount() == 0) {
                throw new ZeroAmountException("Book with id - " + bookId + " has zero amount.");
            }
            book.setAmount(book.getAmount() - 1);
            bookRepository.save(book);
            member.getBorrowedBooks().add(MemberBook.builder().member(member).book(book).build());
            memberRepository.save(member);
            return;
        }
        throw new MaxBorrowedException("Max size of borrowed books by one member - " + maxBorrowedBooks + " was exceed.");
    }

    @Transactional
    public void returnBook(int memberId, int bookId) throws NotFoundException {
        Member member = memberRepository.findById(memberId);
        if(member == null) {
            throw new NotFoundException("Member with id - " + memberId + " was not found.");
        }
        Book book = bookRepository.findById(bookId);
        if(book == null) {
            throw new NotFoundException("Book with id - " + bookId + " was not found.");
        }
        book.setAmount(book.getAmount() + 1);
        bookRepository.save(book);
        for(int i = 0; i < member.getBorrowedBooks().size(); i++) {
            if(member.getBorrowedBooks().get(i).getBook().getId() == bookId) {
                member.getBorrowedBooks().remove(i);
                memberRepository.save(member);
                break;
            }
        }
    }
}
