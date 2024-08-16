package com.example.membershiplibrary.dto.member;

import java.time.LocalDate;
import lombok.Data;

@Data
public class MemberResponseDto {
    private Long id;
    private String name;
    private LocalDate membershipDate;
}
