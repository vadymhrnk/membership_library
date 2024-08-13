package com.example.membershiplibrary.dto.book;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String author;
}
