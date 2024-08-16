package com.example.membershiplibrary.mapper;

import com.example.membershiplibrary.config.MapperConfig;
import com.example.membershiplibrary.dto.book.BorrowedBookResponseDto;
import com.example.membershiplibrary.model.MemberBook;
import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface MemberBookMapper {
    @Mapping(source = "book.id", target = "id")
    @Mapping(source = "book.title", target = "title")
    @Mapping(source = "book.author", target = "author")
    BorrowedBookResponseDto toBorrowedBookResponseDto(MemberBook memberBook);

    @IterableMapping(elementTargetType = BorrowedBookResponseDto.class)
    List<BorrowedBookResponseDto> toBorrowedBookResponseDtoList(List<MemberBook> memberBooks);
}
