package com.example.membershiplibrary.service.book;

import com.example.membershiplibrary.dto.member.CreateMemberRequestDto;
import com.example.membershiplibrary.dto.member.MemberResponseDto;
import com.example.membershiplibrary.dto.member.UpdateMemberRequestDto;
import com.example.membershiplibrary.exception.EntityNotFoundException;
import com.example.membershiplibrary.exception.InvalidVariableException;
import com.example.membershiplibrary.mapper.MemberMapper;
import com.example.membershiplibrary.model.Book;
import com.example.membershiplibrary.model.Member;
import com.example.membershiplibrary.model.MemberBook;
import com.example.membershiplibrary.repository.BookRepository;
import com.example.membershiplibrary.repository.MemberBookRepository;
import com.example.membershiplibrary.repository.MemberRepository;
import com.example.membershiplibrary.service.MemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    public static final String NO_MATCHES_MESSAGE = "No matches with member id %d and book id %d";
    public static final String NO_MEMBER_MESSAGE = "Can't find member with id: ";
    public static final String NO_BOOK_MESSAGE = "Can't find book with id: ";
    public static final String NOT_ENOUGH_BOOKS_MESSAGE = "Not enough books available to borrow";
    public static final String BORROW_LIMIT_MESSAGE = "Can't borrow more than 10 books";
    public static final int BORROW_AMOUNT = 1;
    public static final int RETURN_AMOUNT = 1;
    public static final String NO_BORROWED_BOOKS_MESSAGE = "No borrowed books to return";

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final MemberMapper memberMapper;
    private final MemberBookRepository memberBookRepository;

    @Value("${library.borrow.limit}")
    private int borrowLimit;

    @Override
    @Transactional
    public MemberResponseDto save(CreateMemberRequestDto requestDto) {
        Member member = new Member();
        member.setName(requestDto.getName());
        return memberMapper.toResponseDto(memberRepository.save(member));
    }

    @Override
    public List<MemberResponseDto> getAll(Pageable pageable) {
        Page<Member> members = memberRepository.findAll(pageable);
        return memberMapper.toResponseDtoList(members);
    }

    @Override
    @Transactional
    public MemberResponseDto updateById(Long id, UpdateMemberRequestDto updateMemberRequestDto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NO_MEMBER_MESSAGE + id));

        member.setName(updateMemberRequestDto.getName());
        member.setMembershipDate(updateMemberRequestDto.getMembershipDate());

        return memberMapper.toResponseDto(memberRepository.save(member));
    }

    @Override
    @Transactional
    public void borrowBook(Long memberId, Long bookId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(NO_MEMBER_MESSAGE + memberId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException(NO_BOOK_MESSAGE + bookId));

        if (book.getAmount() < BORROW_AMOUNT) {
            throw new InvalidVariableException(NOT_ENOUGH_BOOKS_MESSAGE);
        }

        int totalBorrowedBooks = memberBookRepository.findByMemberId(memberId).stream()
                .mapToInt(MemberBook::getBorrowedQuantity)
                .sum();

        if (totalBorrowedBooks >= borrowLimit) {
            throw new InvalidVariableException(BORROW_LIMIT_MESSAGE);
        }

        MemberBook memberBook = memberBookRepository.findByMemberIdAndBookId(memberId, bookId)
                .orElse(new MemberBook());

        memberBook.setMember(member);
        memberBook.setBook(book);
        memberBook.setBorrowedQuantity(memberBook.getBorrowedQuantity() + BORROW_AMOUNT);

        memberBookRepository.save(memberBook);

        book.setAmount(book.getAmount() - BORROW_AMOUNT);
        bookRepository.save(book);
    }

    @Override
    @Transactional
    public void returnBook(Long memberId, Long bookId) {
        MemberBook memberBook = memberBookRepository.findByMemberIdAndBookId(memberId, bookId)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                String.format(NO_MATCHES_MESSAGE, memberId, bookId)
                        )
                );

        int currentBorrowedQuantity = memberBook.getBorrowedQuantity();
        if (currentBorrowedQuantity <= 0) {
            throw new InvalidVariableException(NO_BORROWED_BOOKS_MESSAGE);
        }

        memberBook.setBorrowedQuantity(currentBorrowedQuantity - RETURN_AMOUNT);
        memberBookRepository.save(memberBook);

        Book book = memberBook.getBook();
        book.setAmount(book.getAmount() + RETURN_AMOUNT);
        bookRepository.save(book);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NO_MEMBER_MESSAGE + id));

        boolean hasBorrowedBooks = memberBookRepository.findByMemberId(id).stream()
                .anyMatch(memberBook -> memberBook.getBorrowedQuantity() > 0);

        if (hasBorrowedBooks) {
            throw new InvalidVariableException("Can't delete member with borrowed books");
        }

        memberRepository.deleteById(id);
    }
}
