package com.example.membershiplibrary.repository;

import com.example.membershiplibrary.dto.book.TotalBorrowedBookResponseDto;
import com.example.membershiplibrary.model.MemberBook;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberBookRepository extends JpaRepository<MemberBook, Long> {
    Optional<MemberBook> findByMemberIdAndBookId(Long memberId, Long bookId);

    List<MemberBook> findByMemberId(Long memberId);

    @Query("SELECT DISTINCT b.title FROM MemberBook mb JOIN mb.book b")
    List<String> findDistinctBorrowedBookNames();

    @Query("""
            SELECT
                new com.example.membershiplibrary.dto.book.TotalBorrowedBookResponseDto(
                    b.title,
                    SUM(mb.borrowedQuantity)
                )
            FROM MemberBook mb
            JOIN mb.book b
            GROUP BY b.title
            """)
    List<TotalBorrowedBookResponseDto> findDistinctBorrowedBooksAndQuantities();
}
