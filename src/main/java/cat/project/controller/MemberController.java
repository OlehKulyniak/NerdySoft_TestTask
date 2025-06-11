package cat.project.controller;

import cat.project.exception.BookBorrowedException;
import cat.project.exception.NotFoundException;
import cat.project.model.dto.MemberDto;
import cat.project.service.MemberService;
import org.springframework.http.HttpStatus;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<List<MemberDto>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMemberById(@PathVariable int id) {
        try {
            MemberDto memberDto = memberService.getMemberById(id);
            return ResponseEntity.ok(memberDto);
        } catch(NotFoundException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }


    @PostMapping
    public ResponseEntity<MemberDto> saveMember(@RequestBody MemberDto memberDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(memberService.saveMember(memberDto));
    }

    @PutMapping
    public ResponseEntity<?> updateMember(@RequestBody MemberDto memberDto) {
        try {
            MemberDto updatedMember = memberService.updateMember(memberDto);
            return ResponseEntity.ok(updatedMember);
        } catch(NotFoundException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable int id) {
        try {
            memberService.deleteMember(id);
            return ResponseEntity.ok().build();
        } catch(BookBorrowedException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}
