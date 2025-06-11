package cat.project.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cat.project.exception.BookBorrowedException;
import cat.project.exception.NotFoundException;
import cat.project.mapper.AuthorMapper;
import cat.project.mapper.BookMapper;
import cat.project.model.dto.AuthorDto;
import cat.project.model.dto.BookDto;
import cat.project.model.entity.Author;
import cat.project.model.entity.Book;
import cat.project.model.entity.Member;
import cat.project.model.entity.MemberBook;
import cat.project.repository.BookRepository;
import cat.project.repository.MemberBookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Spy
    private BookMapper bookMapper = BookMapper.INSTANCE;

    @Spy
    private AuthorMapper authorMapper = AuthorMapper.INSTANCE;

    @Mock
    private MemberBookRepository memberBookRepository;
    @InjectMocks
    private BookService bookService;

    @Test
    void getAllBooksReturnsListOfDtos() {
        Author author = new Author(1, "Alice Smith");
        Book book = new Book(1, "Title1", author, 5, null);
        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<BookDto> bookDtos = bookService.getAllBooks();

        assertEquals(1, bookDtos.size());
        assertEquals(1, bookDtos.get(0).getId());
        assertEquals("Title1", bookDtos.get(0).getTitle());
        assertEquals(5, bookDtos.get(0).getAmount());
    }

    @Test
    void getBookByIdReturnsDto() throws NotFoundException {
        Author author = new Author(2, "Bob Brown");
        Book book = new Book(3, "TitleNew", author, 2, null);
        when(bookRepository.findById(3)).thenReturn(book);

        BookDto dto = bookService.getBookById(3);

        assertEquals(3, dto.getId());
        assertEquals("TitleNew", dto.getTitle());
        assertEquals(2, dto.getAmount());
    }

    @Test
    void getBookByIdThrowsNotFoundException() {
        when(bookRepository.findById(9)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> bookService.getBookById(9));
    }

    @Test
    void saveBookReturnsDto() {
        AuthorDto authorDto = new AuthorDto(0, "Carol Jones");
        BookDto bookDto = new BookDto(0, "NewTitle", authorDto, 1);
        Author author = new Author(5, "Carol Jones");
        Book savedBook = new Book(20, "NewTitle", author, 1, null);
        when(bookRepository.findByTitleAndAuthor_Name("NewTitle", "Carol Jones")).thenReturn(null);
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        BookDto result = bookService.saveBook(bookDto);

        assertEquals(20, result.getId());
        assertEquals(1, result.getAmount());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void saveBookIncrementsAmount() {
        Author author = new Author(6, "Dan White");
        Book existing = new Book(8, "ExistTitle", author, 3, null);
        when(bookRepository.findByTitleAndAuthor_Name("ExistTitle", "Dan White")).thenReturn(existing);
        when(bookRepository.save(existing)).thenReturn(existing);

        BookDto result = bookService.saveBook(new BookDto(0, "ExistTitle", new AuthorDto(0, "Dan White"), 0));

        assertEquals(4, result.getAmount());
        verify(bookRepository).save(existing);
    }

    @Test
    void updateBookReturnsDto() throws NotFoundException {
        Author authorNew = new Author(9, "Eve Black");
        Book foundBook = new Book(10, "OldTitle", authorNew, 2, null);
        when(bookRepository.findById(10)).thenReturn(foundBook);
        Book updatedBook = new Book(10, "UpdatedTitle", authorNew, 7, null);
        when(bookRepository.save(foundBook)).thenReturn(updatedBook);
        BookDto bookDto = new BookDto(10, "UpdatedTitle", new AuthorDto(0, "Eve Black"), 7);

        BookDto result = bookService.updateBook(bookDto);

        assertEquals(10, result.getId());
        assertEquals("UpdatedTitle", result.getTitle());
        assertEquals(7, result.getAmount());
    }

    @Test
    void updateBookThrowsNotFoundException() {
        when(bookRepository.findById(11)).thenReturn(null);
        assertThrows(NotFoundException.class,
                () -> bookService.updateBook(new BookDto(11, "X", new AuthorDto(0, "A B"), 1)));
    }

    @Test
    void deleteBookDeletesSuccessfully() {
        when(memberBookRepository.findAllByBook_Id(50)).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> bookService.deleteBook(50));
        verify(bookRepository).deleteById(50);
    }

    @Test
    void deleteBookThrowsBookBorrowedException() {
        Member member = new Member(13, "M", null, null);
        Book book = new Book(14, "Some Title", new Author(0, "A B"), 1, null);
        MemberBook memberBook = new MemberBook(null, member, book);
        when(memberBookRepository.findAllByBook_Id(14)).thenReturn(List.of(memberBook));

        assertThrows(BookBorrowedException.class, () -> bookService.deleteBook(14));
    }
}