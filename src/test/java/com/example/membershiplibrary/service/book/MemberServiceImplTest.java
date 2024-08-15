package com.example.membershiplibrary.service.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.membershiplibrary.dto.member.CreateMemberRequestDto;
import com.example.membershiplibrary.dto.member.MemberResponseDto;
import com.example.membershiplibrary.dto.member.UpdateMemberRequestDto;
import com.example.membershiplibrary.exception.InvalidVariableException;
import com.example.membershiplibrary.mapper.MemberMapper;
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
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {
    public static final String MEMBER_SERVICE_PROPERTY_NAME = "borrowLimit";
    public static final int BORROW_LIMIT = 10;
    public static final String EXCLUDE_ID = "id";
    public static final int NUMBER_OF_INVOCATIONS = 1;
    public static final int BORROWED_QUANTITY_ONE = 1;
    public static final int BORROWED_QUANTITY_ZERO = 0;
    public static final int BORROWED_QUANTITY_TEN = 10;
    public static final int MEMBERS_SIZE = 1;
    public static final int PAGE_NUMBER = 0;
    public static final int PAGE_SIZE = 10;
    public static final int FIRST_MEMBER = 0;
    public static final long MEMBER_BOB_ID = 1L;
    public static final String MEMBER_BOB_SMITH = "Bob Smith";
    public static final String UPDATED_BOB_DOE = "Bob Doe";
    public static final String BOOK_THE_BOOK = "The Book";
    public static final long BOOK_THE_BOOK_ID = 1L;
    public static final int BOOK_THE_BOOK_AMOUNT_TEN = 10;
    public static final int BOOK_THE_BOOK_AMOUNT_ZERO = 0;
    public static final int EXPECTED_AMOUNT_RETURNED_THE_BOOK = 11;
    public static final int EXPECTED_AMOUNT_BORROWED_THE_BOOK = 9;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private MemberMapper memberMapper;
    @Mock
    private MemberBookRepository memberBookRepository;
    @InjectMocks
    private MemberServiceImpl memberService;

    private Member member;
    private Book book;
    private MemberResponseDto memberResponseDto;
    private CreateMemberRequestDto createMemberRequestDto;
    private UpdateMemberRequestDto updateMemberRequestDto;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(memberService, MEMBER_SERVICE_PROPERTY_NAME, BORROW_LIMIT);

        member = new Member();
        member.setId(MEMBER_BOB_ID);
        member.setName(MEMBER_BOB_SMITH);

        book = new Book();
        book.setId(BOOK_THE_BOOK_ID);
        book.setTitle(BOOK_THE_BOOK);
        book.setAmount(BOOK_THE_BOOK_AMOUNT_TEN);

        memberResponseDto = new MemberResponseDto();
        memberResponseDto.setId(MEMBER_BOB_ID);
        memberResponseDto.setName(MEMBER_BOB_SMITH);

        createMemberRequestDto = new CreateMemberRequestDto();
        createMemberRequestDto.setName(MEMBER_BOB_SMITH);

        updateMemberRequestDto = new UpdateMemberRequestDto();
        updateMemberRequestDto.setName(UPDATED_BOB_DOE);
    }

    @Test
    void save_shouldSaveNewMember_whenMemberIsValid() {
        when(memberRepository.save(any(Member.class))).thenReturn(member);
        when(memberMapper.toResponseDto(member)).thenReturn(memberResponseDto);

        MemberResponseDto savedMember = memberService.save(createMemberRequestDto);

        assertNotNull(savedMember);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(
                memberResponseDto, savedMember, EXCLUDE_ID
        ));
        verify(memberRepository, times(NUMBER_OF_INVOCATIONS)).save(any(Member.class));
    }

    @Test
    void getAll_shouldReturnAllMembers_whenMembersExist() {
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        Page<Member> membersPage = new PageImpl<>(Collections.singletonList(member));
        when(memberRepository.findAll(pageable)).thenReturn(membersPage);
        when(memberMapper.toResponseDtoList(membersPage))
                .thenReturn(Collections.singletonList(memberResponseDto));

        List<MemberResponseDto> members = memberService.getAll(pageable);

        assertNotNull(members);
        assertEquals(MEMBERS_SIZE, members.size());
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(
                memberResponseDto, members.get(FIRST_MEMBER), EXCLUDE_ID
        ));
        verify(memberRepository, times(NUMBER_OF_INVOCATIONS)).findAll(pageable);
    }

    @Test
    void updateById_shouldUpdateMemberDetails_whenMemberExists() {
        when(memberRepository.findById(MEMBER_BOB_ID)).thenReturn(Optional.of(member));
        when(memberRepository.save(member)).thenReturn(member);
        when(memberMapper.toResponseDto(member)).thenReturn(memberResponseDto);

        MemberResponseDto updatedMember = memberService
                .updateById(MEMBER_BOB_ID, updateMemberRequestDto);

        assertNotNull(updatedMember);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(
                memberResponseDto, updatedMember, EXCLUDE_ID
        ));
        verify(memberRepository, times(NUMBER_OF_INVOCATIONS)).save(member);
    }

    @Test
    void borrowBook_shouldBorrowBook_whenBookIsAvailableAndMemberHasNotReachedLimit() {
        when(memberRepository.findById(MEMBER_BOB_ID)).thenReturn(Optional.of(member));
        when(bookRepository.findById(BOOK_THE_BOOK_ID)).thenReturn(Optional.of(book));
        when(memberBookRepository.findByMemberId(MEMBER_BOB_ID))
                .thenReturn(Collections.emptyList());
        when(memberBookRepository.findByMemberIdAndBookId(MEMBER_BOB_ID, BOOK_THE_BOOK_ID))
                .thenReturn(Optional.empty());

        memberService.borrowBook(MEMBER_BOB_ID, BOOK_THE_BOOK_ID);

        verify(memberBookRepository, times(NUMBER_OF_INVOCATIONS)).save(any(MemberBook.class));
        verify(bookRepository, times(NUMBER_OF_INVOCATIONS)).save(book);
        assertEquals(EXPECTED_AMOUNT_BORROWED_THE_BOOK, book.getAmount());
    }

    @Test
    void borrowBook_shouldThrowException_whenNotEnoughBooksAvailable() {
        book.setAmount(BOOK_THE_BOOK_AMOUNT_ZERO);
        when(memberRepository.findById(MEMBER_BOB_ID)).thenReturn(Optional.of(member));
        when(bookRepository.findById(BOOK_THE_BOOK_ID)).thenReturn(Optional.of(book));

        Exception exception = assertThrows(
                InvalidVariableException.class,
                () -> memberService.borrowBook(MEMBER_BOB_ID, BOOK_THE_BOOK_ID)
        );

        assertEquals(MemberServiceImpl.NOT_ENOUGH_BOOKS_MESSAGE, exception.getMessage());
        verify(memberBookRepository, never()).save(any(MemberBook.class));
        verify(bookRepository, never()).save(book);
    }

    @Test
    void borrowBook_shouldThrowException_whenBorrowLimitExceeded() {
        MemberBook memberBook = new MemberBook();
        memberBook.setBorrowedQuantity(BORROWED_QUANTITY_TEN);

        when(memberRepository.findById(MEMBER_BOB_ID)).thenReturn(Optional.of(member));
        when(bookRepository.findById(BOOK_THE_BOOK_ID)).thenReturn(Optional.of(book));
        when(memberBookRepository.findByMemberId(MEMBER_BOB_ID))
                .thenReturn(Collections.singletonList(memberBook));

        Exception exception = assertThrows(
                InvalidVariableException.class,
                () -> memberService.borrowBook(MEMBER_BOB_ID, BOOK_THE_BOOK_ID)
        );

        assertEquals(MemberServiceImpl.BORROW_LIMIT_MESSAGE, exception.getMessage());
        verify(memberBookRepository, never()).save(any(MemberBook.class));
        verify(bookRepository, never()).save(book);
    }

    @Test
    void returnBook_shouldReturnBook_whenBorrowedQuantityIsGreaterThanZero() {
        MemberBook memberBook = new MemberBook();
        memberBook.setMember(member);
        memberBook.setBook(book);
        memberBook.setBorrowedQuantity(BORROWED_QUANTITY_ONE);

        when(memberBookRepository.findByMemberIdAndBookId(MEMBER_BOB_ID, BOOK_THE_BOOK_ID))
                .thenReturn(Optional.of(memberBook));

        memberService.returnBook(MEMBER_BOB_ID, BOOK_THE_BOOK_ID);

        verify(memberBookRepository, times(NUMBER_OF_INVOCATIONS)).save(memberBook);
        verify(bookRepository, times(NUMBER_OF_INVOCATIONS)).save(book);
        assertEquals(EXPECTED_AMOUNT_RETURNED_THE_BOOK, book.getAmount());
        assertEquals(BORROWED_QUANTITY_ZERO, memberBook.getBorrowedQuantity());
    }

    @Test
    void returnBook_shouldThrowException_whenNoBorrowedBooksToReturn() {
        MemberBook memberBook = new MemberBook();
        memberBook.setBorrowedQuantity(BORROWED_QUANTITY_ZERO);

        when(memberBookRepository.findByMemberIdAndBookId(MEMBER_BOB_ID, BOOK_THE_BOOK_ID))
                .thenReturn(Optional.of(memberBook));

        Exception exception = assertThrows(
                InvalidVariableException.class,
                () -> memberService.returnBook(MEMBER_BOB_ID, BOOK_THE_BOOK_ID)
        );

        assertEquals(MemberServiceImpl.NO_BORROWED_BOOKS_MESSAGE, exception.getMessage());
        verify(memberBookRepository, never()).save(memberBook);
        verify(bookRepository, never()).save(book);
    }

    @Test
    void deleteById_shouldDeleteMember_whenNoBorrowedBooksExist() {
        when(memberRepository.findById(MEMBER_BOB_ID)).thenReturn(Optional.of(member));
        when(memberBookRepository.findByMemberId(MEMBER_BOB_ID))
                .thenReturn(Collections.emptyList());

        memberService.deleteById(MEMBER_BOB_ID);

        verify(memberRepository, times(NUMBER_OF_INVOCATIONS)).deleteById(MEMBER_BOB_ID);
    }

    @Test
    void deleteById_shouldThrowException_whenMemberHasBorrowedBooks() {
        MemberBook memberBook = new MemberBook();
        memberBook.setBorrowedQuantity(BORROWED_QUANTITY_ONE);

        when(memberRepository.findById(MEMBER_BOB_ID)).thenReturn(Optional.of(member));
        when(memberBookRepository.findByMemberId(MEMBER_BOB_ID))
                .thenReturn(Collections.singletonList(memberBook));

        Exception exception = assertThrows(
                InvalidVariableException.class,
                () -> memberService.deleteById(MEMBER_BOB_ID)
        );

        assertEquals(MemberServiceImpl.MEMBER_WITH_BORROWED_BOOKS_MESSAGE, exception.getMessage());
        verify(memberRepository, never()).deleteById(MEMBER_BOB_ID);
    }
}
