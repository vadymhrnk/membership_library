package com.example.membershiplibrary.mapper;

import com.example.membershiplibrary.config.MapperConfig;
import com.example.membershiplibrary.dto.book.BookResponseDto;
import com.example.membershiplibrary.dto.book.CreateBookRequestDto;
import com.example.membershiplibrary.model.Book;
import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    Book toModel(CreateBookRequestDto requestDto);

    BookResponseDto toResponseDto(Book book);

    @IterableMapping(elementTargetType = BookResponseDto.class)
    List<BookResponseDto> toResponseDtoList(Page<Book> books);
}
