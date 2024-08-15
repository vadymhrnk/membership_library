package com.example.membershiplibrary.service.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.membershiplibrary.dto.book.BookResponseDto;
import com.example.membershiplibrary.dto.book.BorrowedBookResponseDto;
import com.example.membershiplibrary.dto.book.CreateBookRequestDto;
import com.example.membershiplibrary.dto.book.UpdateBookRequestDto;
import com.example.membershiplibrary.mapper.BookMapper;
import com.example.membershiplibrary.mapper.MemberBookMapper;
import com.example.membershiplibrary.model.Book;
import com.example.membershiplibrary.model.Member;
import com.example.membershiplibrary.model.MemberBook;
import com.example.membershiplibrary.repository.BookRepository;
import com.example.membershiplibrary.repository.MemberBookRepository;
import com.example.membershiplibrary.repository.MemberRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    public static final String BOOK_1984 = "1984";
    public static final String GEORGE_ORWELL = "George Orwell";
    public static final String EXCLUDE_ID = "id";
    public static final int BORROWED_BOOKS_SIZE = 1;
    public static final int NUMBER_OF_INVOCATIONS = 1;
    public static final String UPDATED_1984 = "Updated 1984";
    public static final String UPDATED_ORWELL = "Updated Orwell";
    public static final String ALICE_WHITE = "Alice White";
    public static final long BOOK_1984_ID = 1L;
    public static final int FIRST_ELEMENT = 0;
    public static final int BORROWED_QUANTITY = 1;
    public static final long MEMBER_ALICE_WHILE_ID = 1L;
    public static final int BOOK_AMOUNT_ONE = 1;
    public static final int BOOK_AMOUNT_TWO = 2;
    public static final int BOOK_AMOUNT_FIVE = 5;
    public static final int EXISTING_BOOKS_SIZE = 1;
    public static final int PAGE_NUMBER = 0;
    public static final int PAGE_SIZE = 10;

    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private MemberBookRepository memberBookRepository;
    @Mock
    private MemberBookMapper memberBookMapper;
    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private BookResponseDto bookResponseDto;
    private CreateBookRequestDto createBookRequestDto;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(BOOK_1984_ID);
        book.setTitle(BOOK_1984);
        book.setAuthor(GEORGE_ORWELL);
        book.setAmount(BOOK_AMOUNT_ONE);

        bookResponseDto = new BookResponseDto();
        bookResponseDto.setId(BOOK_1984_ID);
        bookResponseDto.setTitle(BOOK_1984);
        bookResponseDto.setAuthor(GEORGE_ORWELL);

        createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setTitle(BOOK_1984);
        createBookRequestDto.setAuthor(GEORGE_ORWELL);
    }

    @Test
    void save_shouldSaveNewBook_whenBookDoesNotExist() {
        when(bookMapper.toModel(createBookRequestDto)).thenReturn(book);
        when(bookRepository.findByTitleAndAuthor(book.getTitle(), book.getAuthor()))
                .thenReturn(Optional.empty());
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toResponseDto(book)).thenReturn(bookResponseDto);

        BookResponseDto savedBook = bookService.save(createBookRequestDto);

        assertNotNull(savedBook);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(
                bookResponseDto, savedBook, EXCLUDE_ID
        ));
        verify(bookRepository, times(NUMBER_OF_INVOCATIONS)).save(book);
    }

    @Test
    void save_shouldUpdateBookAmount_whenBookAlreadyExists() {
        Book existingBook = new Book();
        existingBook.setId(BOOK_1984_ID);
        existingBook.setTitle(BOOK_1984);
        existingBook.setAuthor(GEORGE_ORWELL);
        existingBook.setAmount(BOOK_AMOUNT_ONE);

        when(bookMapper.toModel(createBookRequestDto)).thenReturn(book);
        when(bookRepository.findByTitleAndAuthor(book.getTitle(), book.getAuthor()))
                .thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(existingBook);
        when(bookMapper.toResponseDto(existingBook)).thenReturn(bookResponseDto);

        BookResponseDto updatedBook = bookService.save(createBookRequestDto);

        assertNotNull(updatedBook);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(
                bookResponseDto, updatedBook, EXCLUDE_ID
        ));
        assertEquals(BOOK_AMOUNT_TWO, existingBook.getAmount());
        verify(bookRepository, times(NUMBER_OF_INVOCATIONS)).save(existingBook);
    }

    @Test
    void getAll_shouldReturnAllBooks_whenBooksExist() {
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        Page<Book> booksPage = new PageImpl<>(Collections.singletonList(book));
        when(bookRepository.findAll(pageable)).thenReturn(booksPage);
        when(bookMapper.toResponseDtoList(booksPage))
                .thenReturn(Collections.singletonList(bookResponseDto));

        List<BookResponseDto> books = bookService.getAll(pageable);

        assertNotNull(books);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(
                bookResponseDto, books.get(FIRST_ELEMENT)
        ));
        assertEquals(EXISTING_BOOKS_SIZE, books.size());
        verify(bookRepository, times(NUMBER_OF_INVOCATIONS)).findAll(pageable);
    }

    @Test
    void updateById_shouldUpdateBookDetails_whenBookExists() {
        UpdateBookRequestDto updateRequestDto = new UpdateBookRequestDto();
        updateRequestDto.setTitle(UPDATED_1984);
        updateRequestDto.setAuthor(UPDATED_ORWELL);
        updateRequestDto.setAmount(BOOK_AMOUNT_FIVE);

        when(bookRepository.findById(BOOK_1984_ID)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toResponseDto(book)).thenReturn(bookResponseDto);

        BookResponseDto updatedBook = bookService.updateById(BOOK_1984_ID, updateRequestDto);

        assertNotNull(updatedBook);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(
                bookResponseDto, updatedBook, EXCLUDE_ID
        ));
        assertEquals(UPDATED_1984, book.getTitle());
        verify(bookRepository, times(NUMBER_OF_INVOCATIONS)).save(book);
    }

    @Test
    void getAllBooksBorrowedByMember_shouldReturnBorrowedBooks_whenMemberExists() {
        Member member = new Member();
        member.setId(MEMBER_ALICE_WHILE_ID);
        member.setName(ALICE_WHITE);

        MemberBook memberBook = new MemberBook();
        memberBook.setBook(book);
        memberBook.setMember(member);

        BorrowedBookResponseDto borrowedBookResponseDto = new BorrowedBookResponseDto();
        borrowedBookResponseDto.setId(BOOK_1984_ID);
        borrowedBookResponseDto.setTitle(BOOK_1984);
        borrowedBookResponseDto.setAuthor(GEORGE_ORWELL);
        borrowedBookResponseDto.setBorrowedQuantity(BORROWED_QUANTITY);

        when(memberRepository.findByName(ALICE_WHITE)).thenReturn(Optional.of(member));
        when(memberBookRepository.findByMemberId(member.getId()))
                .thenReturn(Collections.singletonList(memberBook));
        when(memberBookMapper.toBorrowedBookResponseDtoList(Collections.singletonList(memberBook)))
                .thenReturn(Collections.singletonList(borrowedBookResponseDto));

        List<BorrowedBookResponseDto> borrowedBooks = bookService
                .getAllBooksBorrowedByMember(ALICE_WHITE);

        assertNotNull(borrowedBooks);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(
                borrowedBookResponseDto, borrowedBooks.get(FIRST_ELEMENT)
        ));

        assertEquals(BORROWED_BOOKS_SIZE, borrowedBooks.size());
        verify(memberRepository, times(NUMBER_OF_INVOCATIONS)).findByName(ALICE_WHITE);
        verify(memberBookRepository, times(NUMBER_OF_INVOCATIONS)).findByMemberId(member.getId());
    }

    @Test
    void deleteById_shouldDeleteBook_whenBookExists() {
        doNothing().when(bookRepository).deleteById(BOOK_1984_ID);

        bookService.deleteById(BOOK_1984_ID);

        verify(bookRepository, times(NUMBER_OF_INVOCATIONS)).deleteById(BOOK_1984_ID);
    }
}
