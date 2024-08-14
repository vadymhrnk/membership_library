package com.example.membershiplibrary.dto.member;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Data;

@Data
public class UpdateMemberRequestDto {
    @NotBlank
    private String name;
    private LocalDate membershipDate;
}
