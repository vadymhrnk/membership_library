package com.example.membershiplibrary.service.book;

import com.example.membershiplibrary.dto.book.BookResponseDto;
import com.example.membershiplibrary.dto.book.CreateBookRequestDto;
import com.example.membershiplibrary.dto.book.UpdateBookRequestDto;
import com.example.membershiplibrary.service.BookService;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    @Override
    public BookResponseDto save(CreateBookRequestDto requestDto) {
        return null;
    }

    @Override
    public List<BookResponseDto> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public BookResponseDto updateById(Long id, UpdateBookRequestDto updateRequestDto) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
