package com.example.membershiplibrary.mapper;

import com.example.membershiplibrary.config.MapperConfig;
import com.example.membershiplibrary.dto.member.MemberResponseDto;
import com.example.membershiplibrary.model.Member;
import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(config = MapperConfig.class)
public interface MemberMapper {
    MemberResponseDto toResponseDto(Member member);

    @IterableMapping(elementTargetType = MemberResponseDto.class)
    List<MemberResponseDto> toResponseDtoList(Page<Member> members);
}
