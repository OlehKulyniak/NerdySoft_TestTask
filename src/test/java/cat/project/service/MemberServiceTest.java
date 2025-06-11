package cat.project.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import cat.project.exception.BookBorrowedException;
import cat.project.exception.NotFoundException;
import cat.project.mapper.MemberMapper;
import cat.project.model.dto.MemberDto;
import cat.project.model.entity.Author;
import cat.project.model.entity.Book;
import cat.project.model.entity.Member;
import cat.project.model.entity.MemberBook;
import cat.project.repository.MemberBookRepository;
import cat.project.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private MemberBookRepository memberBookRepository;
    @Spy
    private MemberMapper memberMapper = MemberMapper.INSTANCE;
    @InjectMocks
    private MemberService memberService;

    @Test
    void getAllMembersReturnsListOfDtos() {
        Member firstMember = new Member(1, "Alice", LocalDate.now(), null);
        Member secondMember = new Member(2, "Bob", LocalDate.now(), null);
        when(memberRepository.findAll()).thenReturn(List.of(firstMember, secondMember));

        List<MemberDto> memberDtos = memberService.getAllMembers();

        assertEquals(2, memberDtos.size());
        assertEquals("Alice", memberDtos.get(0).getName());
        assertEquals("Bob", memberDtos.get(1).getName());
    }

    @Test
    void getMemberByIdReturnsDto() throws NotFoundException {
        Member member = new Member(3, "Charlie", LocalDate.now(), null);
        when(memberRepository.findById(3)).thenReturn(member);

        MemberDto memberDto = memberService.getMemberById(3);

        assertEquals(3, memberDto.getId());
        assertEquals("Charlie", memberDto.getName());
    }

    @Test
    void getMemberByIdThrowsNotFoundException() {
        when(memberRepository.findById(4)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> memberService.getMemberById(4));
    }

    @Test
    void saveMemberReturnsDto() {
        Member savedMember = new Member(5, "Dave", LocalDate.now(), null);
        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);

        MemberDto memberDto = memberService.saveMember(new MemberDto(0, "Dave"));

        assertEquals(5, memberDto.getId());
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    void updateMemberReturnsDto() throws NotFoundException {
        Member foundMember = new Member(6, "Eve", LocalDate.now(), null);
        when(memberRepository.findById(6)).thenReturn(foundMember);
        Member updatedMember = new Member(6, "EveNew", LocalDate.now(), null);
        when(memberRepository.save(foundMember)).thenReturn(updatedMember);

        MemberDto memberDto = memberService.updateMember(new MemberDto(6, "EveNew"));

        assertEquals("EveNew", memberDto.getName());
    }

    @Test
    void updateMemberThrowsNotFoundException() {
        when(memberRepository.findById(7)).thenReturn(null);
        assertThrows(NotFoundException.class,
                () -> memberService.updateMember(new MemberDto(7, "X")));
    }

    @Test
    void deleteMemberDeletesSuccessfully() {
        when(memberBookRepository.findAllByMember_Id(8)).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> memberService.deleteMember(8));
        verify(memberRepository).deleteById(8);
    }

    @Test
    void deleteMemberThrowsBookBorrowedException() {
        Member member = new Member(9, "Fred", LocalDate.now(), null);
        Book book = new Book(10, "T", new Author(0, "A B"), 1, null);
        MemberBook memberBook = new MemberBook(null, member, book);
        when(memberBookRepository.findAllByMember_Id(9)).thenReturn(List.of(memberBook));

        assertThrows(BookBorrowedException.class, () -> memberService.deleteMember(9));
    }
}