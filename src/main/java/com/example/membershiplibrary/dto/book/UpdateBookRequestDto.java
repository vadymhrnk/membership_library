package com.example.membershiplibrary.dto.book;

import com.example.membershiplibrary.annotation.book.ValidAuthorName;
import com.example.membershiplibrary.annotation.book.ValidBookTitle;
import lombok.Data;

@Data
public class UpdateBookRequestDto {
    @ValidBookTitle
    private String title;
    @ValidAuthorName
    private String author;
    private int amount;
}
