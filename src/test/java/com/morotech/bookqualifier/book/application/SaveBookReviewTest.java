package com.morotech.bookqualifier.book.application;

import com.morotech.bookqualifier.book.domain.builder.BookReviewDtoBuilder;
import com.morotech.bookqualifier.book.domain.repository.BookReviewRepository;
import com.morotech.bookqualifier.book.portadapter.dto.BookReviewDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class SaveBookReviewTest {

    private BookReviewRepository bookReviewRepository;

    @BeforeEach
    void init() {
        bookReviewRepository = mock(BookReviewRepository.class);
    }

    @Test
    void it_should_save_book_review() {
        BookReviewDto bookReviewDto = new BookReviewDtoBuilder().create();
        SaveBookReview saveBookReview = new SaveBookReview(bookReviewRepository);

        saveBookReview.save(bookReviewDto);

        verify(bookReviewRepository, times(1)).save(any());
    }

    @Test
    void it_should_return_200_when_everything_is_ok() {
        BookReviewDto bookReviewDto = new BookReviewDtoBuilder().create();
        SaveBookReview saveBookReview = new SaveBookReview(bookReviewRepository);

        ResponseEntity<String> response = saveBookReview.save(bookReviewDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void it_should_return_400_when_a_domain_exception_occurred() {
        BookReviewDto bookReviewDto = new BookReviewDtoBuilder().create();
        bookReviewDto.bookId = -1;
        SaveBookReview saveBookReview = new SaveBookReview(bookReviewRepository);

        ResponseEntity<String> response = saveBookReview.save(bookReviewDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void it_should_return_500_when_unexpected_error_occurred() {
        BookReviewDto bookReviewDto = new BookReviewDtoBuilder().create();
        SaveBookReview saveBookReview = new SaveBookReview(bookReviewRepository);
        when(bookReviewRepository.save(any())).thenThrow(new RuntimeException("Something whent wrong..."));

        ResponseEntity<String> response = saveBookReview.save(bookReviewDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
