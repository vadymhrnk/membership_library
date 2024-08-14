package com.example.membershiplibrary.service;

import com.example.membershiplibrary.dto.member.CreateMemberRequestDto;
import com.example.membershiplibrary.dto.member.MemberResponseDto;
import com.example.membershiplibrary.dto.member.UpdateMemberRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface MemberService {
    MemberResponseDto save(CreateMemberRequestDto requestDto);

    List<MemberResponseDto> getAll(Pageable pageable);

    MemberResponseDto updateById(Long id, UpdateMemberRequestDto requestDto);

    void borrowBook(Long memberId, Long bookId);

    void returnBook(Long memberId, Long bookId);

    void deleteById(Long id);
}
