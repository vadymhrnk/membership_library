package com.example.membershiplibrary.service.book;

import com.example.membershiplibrary.dto.book.BookResponseDto;
import com.example.membershiplibrary.dto.book.CreateBookRequestDto;
import com.example.membershiplibrary.dto.book.UpdateBookRequestDto;
import com.example.membershiplibrary.exception.EntityNotFoundException;
import com.example.membershiplibrary.mapper.BookMapper;
import com.example.membershiplibrary.model.Book;
import com.example.membershiplibrary.repository.BookRepository;
import com.example.membershiplibrary.service.BookService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public BookResponseDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);

        Optional<Book> optionalBook = bookRepository.findByTitleAndAuthor(
                book.getTitle(),
                book.getAuthor()
        );

        if (optionalBook.isPresent()) {
            Book existingBook = optionalBook.get();
            existingBook.setAmount(existingBook.getAmount() + 1);
            return bookMapper.toResponseDto(bookRepository.save(existingBook));
        }

        book.setAmount(1);
        return bookMapper.toResponseDto(bookRepository.save(book));
    }

    @Override
    public List<BookResponseDto> getAll(Pageable pageable) {
        Page<Book> books = bookRepository.findAll(pageable);
        return bookMapper.toResponseDtoList(books);
    }

    @Override
    @Transactional
    public BookResponseDto updateById(Long id, UpdateBookRequestDto updateRequestDto) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book with id: " + id)
        );
        book.setTitle(updateRequestDto.getTitle());
        book.setAuthor(updateRequestDto.getAuthor());
        book.setAmount(updateRequestDto.getAmount());
        return bookMapper.toResponseDto(bookRepository.save(book));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
