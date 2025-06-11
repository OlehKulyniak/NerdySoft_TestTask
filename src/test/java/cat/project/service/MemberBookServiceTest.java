package cat.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cat.project.exception.MaxBorrowedException;
import cat.project.exception.NotFoundException;
import cat.project.exception.ZeroAmountException;
import cat.project.mapper.BookMapper;
import cat.project.model.dto.BookDto;
import cat.project.model.dto.BorrowedBookDto;
import cat.project.model.entity.Author;
import cat.project.model.entity.Book;
import cat.project.model.entity.Member;
import cat.project.model.entity.MemberBook;
import cat.project.repository.BookRepository;
import cat.project.repository.MemberBookRepository;
import cat.project.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class MemberBookServiceTest {
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private MemberBookRepository memberBookRepository;
    @Spy
    private BookMapper bookMapper = BookMapper.INSTANCE;
    @InjectMocks
    private MemberBookService service;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(service, "maxBorrowedBooks", "3");
    }

    @Test
    void getBorrowedBooksByMemberNameReturnsListOfDtos() {
        Book book = new Book(1, "Title1", new Author(0, "A B"), 2, null);
        when(memberBookRepository.findAllBookByMember_Name("John")).thenReturn(List.of(book));

        List<BookDto> dtos = service.getBorrowedBooksByMemberName("John");

        assertEquals(1, dtos.size());
        assertEquals(1, dtos.get(0).getId());
        assertEquals("Title1", dtos.get(0).getTitle());
    }

    @Test
    void getDistinctBorrowedBookNamesReturnsListOfNames() {
        List<String> names = Arrays.asList("T1", "T2");
        when(memberBookRepository.findAllDistinctBook_Title()).thenReturn(names);

        List<String> result = service.getDistinctBorrowedBookNames();
        assertEquals(names, result);
    }

    @Test
    void getDistinctBorrowedBookNamesAndCountReturnsRawList() {
        List<BorrowedBookDto> counts = List.of(new BorrowedBookDto("T1", 2));
        when(memberBookRepository.findAllDistinctBook_TitleAndCount()).thenReturn(counts);

        List<BorrowedBookDto> result = service.getDistinctBorrowedBookNamesAndCount();
        assertEquals(counts, result);
    }

    @Test
    void borrowBookSuccessfulBorrow() throws Exception {
        Member member = new Member(1, "John", LocalDate.now(), new ArrayList<>());
        Book book = new Book(2, "Title2", new Author(0, "A B"), 1, null);
        when(memberRepository.findById(1)).thenReturn(member);
        when(bookRepository.findById(2)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(memberRepository.save(member)).thenReturn(member);

        service.borrowBook(1, 2);

        verify(bookRepository).save(book);
        verify(memberRepository).save(member);
    }

    @Test
    void borrowBookMemberThrowsNotFoundException() {
        when(memberRepository.findById(1)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> service.borrowBook(1, 2));
    }

    @Test
    void borrowBookBookThrowsNotFoundException() {
        Member member = new Member(1, "John", LocalDate.now(), new ArrayList<>());
        when(memberRepository.findById(1)).thenReturn(member);
        when(bookRepository.findById(2)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> service.borrowBook(1, 2));
    }

    @Test
    void borrowBookBookZeroAmountThrowsZeroAmountException() {
        Member member = new Member(1, "John", LocalDate.now(), new ArrayList<>());
        Book book = new Book(2, "Title2", new Author(0, "A B"), 0, null);
        when(memberRepository.findById(1)).thenReturn(member);
        when(bookRepository.findById(2)).thenReturn(book);
        assertThrows(ZeroAmountException.class, () -> service.borrowBook(1, 2));
    }

    @Test
    void borrowBookMaxExceededThrowsMaxBorrowedException() {
        Member member = new Member(1, "John", LocalDate.now(), new ArrayList<>());

        for (int i = 0; i < 3; i++) {
            member.getBorrowedBooks().add(new MemberBook(null, member,
                    new Book(i, "T", new Author(0, "A B"), 1, null)));
        }
        when(memberRepository.findById(1)).thenReturn(member);
        assertThrows(MaxBorrowedException.class, () -> service.borrowBook(1, 2));
    }

    @Test
    void returnBookSuccessfulReturn() throws Exception {
        Member member = new Member(1, "John", LocalDate.now(), new ArrayList<>());
        Book book = new Book(2, "Title2", new Author(0, "A B"), 0, null);
        MemberBook mb = new MemberBook(null, member, book);
        member.getBorrowedBooks().add(mb);
        when(memberRepository.findById(1)).thenReturn(member);
        when(bookRepository.findById(2)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(memberRepository.save(member)).thenReturn(member);

        service.returnBook(1, 2);

        verify(bookRepository).save(book);
        verify(memberRepository).save(member);
    }

    @Test
    void returnBookMemberThrowsNotFoundException() {
        when(memberRepository.findById(1)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> service.returnBook(1, 2));
    }

    @Test
    void returnBookBookThrowsNotFoundException() {
        Member member = new Member(1, "John", LocalDate.now(), new ArrayList<>());
        when(memberRepository.findById(1)).thenReturn(member);
        when(bookRepository.findById(2)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> service.returnBook(1, 2));
    }
}