package cat.project.service;

import cat.project.exception.BookBorrowedException;
import cat.project.exception.NotFoundException;
import cat.project.mapper.MemberMapper;
import cat.project.model.dto.MemberDto;
import cat.project.model.entity.Member;
import cat.project.model.entity.MemberBook;
import cat.project.repository.MemberBookRepository;
import cat.project.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberBookRepository memberBookRepository;
    private final MemberMapper memberMapper;

    public List<MemberDto> getAllMembers() {
        List<Member> members = memberRepository.findAll();
        List<MemberDto> memberDtos = new ArrayList<>();
        for(var member : members) {
            memberDtos.add(memberMapper.memberToDto(member));
        }
        return memberDtos;
    }

    public MemberDto getMemberById(int id) throws NotFoundException {
        Member member = memberRepository.findById(id);
        if(member == null) {
            throw new NotFoundException("Member with id - " + id + " was not found.");
        }
        return memberMapper.memberToDto(member);
    }

    @Transactional
    public MemberDto saveMember(MemberDto memberDto) {
        Member member = memberMapper.dtoToMember(memberDto);
        return memberMapper.memberToDto(memberRepository.save(member));
    }

    @Transactional
    public MemberDto updateMember(MemberDto memberDto) throws NotFoundException {
        Member foundMember = memberRepository.findById(memberDto.getId());
        if(foundMember == null) {
            throw new NotFoundException("Member with id - " + memberDto.getId() + " was not found.");
        }
        foundMember.setName(memberDto.getName());
        return memberMapper.memberToDto(memberRepository.save(foundMember));
    }

    @Transactional
    public void deleteMember(int id) throws BookBorrowedException {
        List<MemberBook> memberBooks = memberBookRepository.findAllByMember_Id(id);
        if(memberBooks.isEmpty()) {
            memberRepository.deleteById(id);
            return;
        }
        String bookIds = memberBooks.stream().map(elem -> elem.getBook().getId()).toString();
        throw new BookBorrowedException("Member with id - " + id + " borrowed these books " + bookIds);
    }
}
