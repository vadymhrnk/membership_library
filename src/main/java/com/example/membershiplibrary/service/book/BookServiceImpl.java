package com.example.membershiplibrary.service.book;

import com.example.membershiplibrary.dto.book.BookResponseDto;
import com.example.membershiplibrary.dto.book.BorrowedBookResponseDto;
import com.example.membershiplibrary.dto.book.CreateBookRequestDto;
import com.example.membershiplibrary.dto.book.TotalBorrowedBookResponseDto;
import com.example.membershiplibrary.dto.book.UpdateBookRequestDto;
import com.example.membershiplibrary.exception.EntityNotFoundException;
import com.example.membershiplibrary.mapper.BookMapper;
import com.example.membershiplibrary.mapper.MemberBookMapper;
import com.example.membershiplibrary.model.Book;
import com.example.membershiplibrary.model.Member;
import com.example.membershiplibrary.model.MemberBook;
import com.example.membershiplibrary.repository.BookRepository;
import com.example.membershiplibrary.repository.MemberBookRepository;
import com.example.membershiplibrary.repository.MemberRepository;
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
    public static final int AMOUNT_TO_ADD = 1;
    public static final String NO_BOOK_MESSAGE = "Can't find book with id: ";
    public static final String NO_MEMBER_MESSAGE = "Can't find member with name: ";

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final MemberBookRepository memberBookRepository;
    private final MemberBookMapper memberBookMapper;

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
            existingBook.setAmount(existingBook.getAmount() + AMOUNT_TO_ADD);
            return bookMapper.toResponseDto(bookRepository.save(existingBook));
        }

        book.setAmount(AMOUNT_TO_ADD);
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
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NO_BOOK_MESSAGE + id));

        book.setTitle(updateRequestDto.getTitle());
        book.setAuthor(updateRequestDto.getAuthor());
        book.setAmount(updateRequestDto.getAmount());

        return bookMapper.toResponseDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public List<BorrowedBookResponseDto> getAllBooksBorrowedByMember(String name) {
        Member member = memberRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException(NO_MEMBER_MESSAGE + name));

        List<MemberBook> borrowedBooks = memberBookRepository.findByMemberId(member.getId());

        return memberBookMapper.toBorrowedBookResponseDtoList(borrowedBooks);
    }

    @Override
    public List<String> getDistinctBorrowedBookNames() {
        return memberBookRepository.findDistinctBorrowedBookNames();
    }

    @Override
    public List<TotalBorrowedBookResponseDto> getDistinctBorrowedBooksAndQuantities() {
        return memberBookRepository.findDistinctBorrowedBooksAndQuantities();
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
