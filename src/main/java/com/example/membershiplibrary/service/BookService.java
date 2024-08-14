package com.example.membershiplibrary.service;

import com.example.membershiplibrary.dto.book.BookResponseDto;
import com.example.membershiplibrary.dto.book.BorrowedBookResponseDto;
import com.example.membershiplibrary.dto.book.CreateBookRequestDto;
import com.example.membershiplibrary.dto.book.TotalBorrowedBookResponseDto;
import com.example.membershiplibrary.dto.book.UpdateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponseDto save(CreateBookRequestDto requestDto);

    List<BookResponseDto> getAll(Pageable pageable);

    BookResponseDto updateById(Long id, UpdateBookRequestDto updateRequestDto);

    List<BorrowedBookResponseDto> getAllBooksBorrowedByMember(String name);

    List<String> getDistinctBorrowedBookNames();

    List<TotalBorrowedBookResponseDto> getDistinctBorrowedBooksAndQuantities();

    void deleteById(Long id);
}
