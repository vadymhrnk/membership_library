package com.example.membershiplibrary.mapper;

import com.example.membershiplibrary.config.MapperConfig;
import com.example.membershiplibrary.dto.book.BookResponseDto;
import com.example.membershiplibrary.dto.book.CreateBookRequestDto;
import com.example.membershiplibrary.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    Book toModel(CreateBookRequestDto requestDto);

    BookResponseDto toResponseDto(Book book);
}
