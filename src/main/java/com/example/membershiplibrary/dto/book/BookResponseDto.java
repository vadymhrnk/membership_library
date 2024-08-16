package com.example.membershiplibrary.dto.book;

import lombok.Data;

@Data
public class BookResponseDto {
    private Long id;
    private String title;
    private String author;
    private int amount;
}
