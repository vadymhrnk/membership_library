package com.example.membershiplibrary.controller;

import com.example.membershiplibrary.dto.member.CreateMemberRequestDto;
import com.example.membershiplibrary.dto.member.MemberResponseDto;
import com.example.membershiplibrary.dto.member.UpdateMemberRequestDto;
import com.example.membershiplibrary.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member manager", description = "Endpoint to manage members")
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Create a new member", description = "Create a new member")
    public MemberResponseDto save(@RequestBody @Valid CreateMemberRequestDto requestDto) {
        return memberService.save(requestDto);
    }

    @GetMapping
    @Operation(summary = "Get all members", description = "Get a list of all members")
    public List<MemberResponseDto> getAll(Pageable pageable) {
        return memberService.getAll(pageable);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a member", description = "Update existing member")
    public MemberResponseDto updateMember(
            @PathVariable Long id,
            @RequestBody @Valid UpdateMemberRequestDto updateMemberRequestDto
    ) {
        return memberService.updateById(id, updateMemberRequestDto);
    }

    @PostMapping("/{memberId}/borrow/{bookId}")
    @Operation(summary = "Borrow a book", description = "Borrow a book by a member")
    public void borrowBook(@PathVariable Long memberId, @PathVariable Long bookId) {
        memberService.borrowBook(memberId, bookId);
    }

    @PostMapping("/{memberId}/return/{bookId}")
    @Operation(summary = "Return a book", description = "Return a book by a member")
    public void returnBook(@PathVariable Long memberId, @PathVariable Long bookId) {
        memberService.returnBook(memberId, bookId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a member", description = "Delete a member by ID")
    public void delete(@PathVariable Long id) {
        memberService.deleteById(id);
    }
}
