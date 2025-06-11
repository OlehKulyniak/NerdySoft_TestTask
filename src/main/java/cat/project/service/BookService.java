package cat.project.service;

import cat.project.exception.BookBorrowedException;
import cat.project.mapper.AuthorMapper;
import cat.project.mapper.BookMapper;
import cat.project.model.dto.BookDto;
import cat.project.model.entity.Book;
import cat.project.exception.NotFoundException;

import cat.project.model.entity.MemberBook;
import cat.project.repository.BookRepository;
import cat.project.repository.MemberBookRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final MemberBookRepository memberBookRepository;
    private final BookMapper bookMapper;
    private final AuthorMapper authorMapper;

    public List<BookDto> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        List<BookDto> bookDtos = new ArrayList<>();
        for(var book : books) {
            bookDtos.add(bookMapper.bookToDto(book));
        }
        return bookDtos;
    }

    public BookDto getBookById(int id) throws NotFoundException {
        Book book = bookRepository.findById(id);
        if(book == null) {
            throw new NotFoundException("Book with id - " + id + " was not found.");
        }
        return bookMapper.bookToDto(book);
    }

    @Transactional
    public BookDto saveBook(BookDto bookDto) {
        Book foundBook = bookRepository.findByTitleAndAuthor_Name(bookDto.getTitle(), bookDto.getAuthorDto().getName());
        Book resultBook;
        if(foundBook != null) {
            foundBook.setAmount(foundBook.getAmount() + 1);
            resultBook = bookRepository.save(foundBook);
        } else {
            Book book = bookMapper.dtoToBook(bookDto);
            resultBook = bookRepository.save(book);
        }
        return bookMapper.bookToDto(resultBook);
    }

    @Transactional
    public BookDto updateBook(BookDto bookDto) throws NotFoundException {
        Book foundBook = bookRepository.findById(bookDto.getId());
        if(foundBook == null) {
            throw new NotFoundException("Book with id - " + bookDto.getId() + " was not found.");
        }
        foundBook.setTitle(bookDto.getTitle());
        foundBook.setAuthor(authorMapper.dtoToAuthor(bookDto.getAuthorDto()));
        foundBook.setAmount(bookDto.getAmount());
        return bookMapper.bookToDto(bookRepository.save(foundBook));
    }

    @Transactional
    public void deleteBook(int id) throws BookBorrowedException {
        List<MemberBook> memberBooks = memberBookRepository.findAllByBook_Id(id);
        if(memberBooks.isEmpty()) {
            bookRepository.deleteById(id);
            return;
        }
        String memberIds = (memberBooks.stream().map(elem -> elem.getMember().getId()).toString());
        throw new BookBorrowedException("Book with id - " + id + " had borrowed by these members - " + memberIds);
    }
}
