package com.example.membershiplibrary.service.book;

import com.example.membershiplibrary.dto.book.BookResponseDto;
import com.example.membershiplibrary.dto.book.CreateBookRequestDto;
import com.example.membershiplibrary.dto.book.UpdateBookRequestDto;
import com.example.membershiplibrary.mapper.BookMapper;
import com.example.membershiplibrary.model.Book;
import com.example.membershiplibrary.repository.BookRepository;
import com.example.membershiplibrary.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;

    @Override
    public BookResponseDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toResponseDto(bookRepository.save(book));
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
