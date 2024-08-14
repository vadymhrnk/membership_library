package com.example.membershiplibrary.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateMemberRequestDto {
    @NotBlank
    private String name;
}
