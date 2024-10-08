package com.example.membershiplibrary.controller;

import com.example.membershiplibrary.dto.book.BookResponseDto;
import com.example.membershiplibrary.dto.book.BorrowedBookResponseDto;
import com.example.membershiplibrary.dto.book.CreateBookRequestDto;
import com.example.membershiplibrary.dto.book.TotalBorrowedBookResponseDto;
import com.example.membershiplibrary.dto.book.UpdateBookRequestDto;
import com.example.membershiplibrary.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book manager", description = "Endpoint to manage books")
@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Create a new book", description = "Create a new book")
    public BookResponseDto save(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.save(requestDto);
    }

    @GetMapping
    @Operation(summary = "Get all books", description = "Get a list of all available books")
    public List<BookResponseDto> getAll(Pageable pageable) {
        return bookService.getAll(pageable);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a book", description = "Update existing book")
    public BookResponseDto updateBook(
            @PathVariable Long id,
            @RequestBody @Valid UpdateBookRequestDto requestDto
    ) {
        return bookService.updateById(id, requestDto);
    }

    @GetMapping("/borrowed-books/member")
    @Operation(
            summary = "Retrieve all books borrowed by member name",
            description = "Retrieve all books borrowed by a specific member by name"
    )
    public List<BorrowedBookResponseDto> getAllBooksBorrowedByMember(@RequestParam String name) {
        return bookService.getAllBooksBorrowedByMember(name);
    }

    @GetMapping("/borrowed-books/distinct")
    @Operation(
            summary = "Retrieve all distinct borrowed books",
            description = "Retrieve all borrowed distinct book names"
    )
    public List<String> getDistinctBorrowedBookNames() {
        return bookService.getDistinctBorrowedBookNames();
    }

    @GetMapping("/borrowed-books/summary")
    @Operation(
            summary = "Retrieve all borrowed distinct books and quantities",
            description = "Retrieve all borrowed distinct book names"
                    + " and amount how much copy of this book was borrowed"
    )
    public List<TotalBorrowedBookResponseDto> getDistinctBorrowedBooksAndQuantities() {
        return bookService.getDistinctBorrowedBooksAndQuantities();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book by id", description = "Delete book by id")
    public void delete(@PathVariable Long id) {
        bookService.deleteById(id);
    }
}
