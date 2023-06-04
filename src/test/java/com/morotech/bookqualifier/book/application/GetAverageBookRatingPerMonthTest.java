package com.morotech.bookqualifier.book.application;

import com.github.javafaker.Faker;
import com.morotech.bookqualifier.book.domain.repository.BookReviewRepository;
import com.morotech.bookqualifier.book.portadapter.dto.AverageBookReviewByMonthResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetAverageBookRatingPerMonthTest {

    private BookReviewRepository bookReviewRepository;
    private Faker faker;

    @BeforeEach
    void init() {
        bookReviewRepository = mock(BookReviewRepository.class);
        faker = new Faker();
    }

    @Test
    void it_should_return_book_review_rating_per_month() {
        GetAveregeBookRatingPerMonth getAverageBookRatingPerMonth =
                new GetAveregeBookRatingPerMonth(bookReviewRepository);
        int bookId = faker.number().numberBetween(1, 100);
        String month = "2023-05-01";
        double rating = 1.0;
        when(bookReviewRepository.findAverageMonthBookReviewById(bookId)).thenReturn(List.of(Map.of(
                "dateMonth", month, "averageRating", rating)));

        AverageBookReviewByMonthResponseDto averageBookReviewByMonthResponseDto = getAverageBookRatingPerMonth
                .getAverageBookRatingPerMonth(bookId);

        assertEquals(HttpStatus.OK, averageBookReviewByMonthResponseDto.responseStatus);
        assertEquals("Average book rating per month retrieved successfully!", averageBookReviewByMonthResponseDto.responseMessage);
        assertEquals(month, averageBookReviewByMonthResponseDto.averageBookReviewByMonthDtoList.get(0).date);
        assertEquals(rating, averageBookReviewByMonthResponseDto.averageBookReviewByMonthDtoList.get(0).averageRating);
    }

    @Test
    void it_should_return_200_when_everything_is_ok() {
        GetAveregeBookRatingPerMonth getAverageBookRatingPerMonth =
                new GetAveregeBookRatingPerMonth(bookReviewRepository);
        int bookId = faker.number().numberBetween(1, 100);
        String month = "2023-05-01";
        double rating = 1.0;
        when(bookReviewRepository.findAverageMonthBookReviewById(bookId)).thenReturn(List.of(Map.of(
                "dateMonth", month, "averageRating", rating)));

        AverageBookReviewByMonthResponseDto averageBookReviewByMonthResponseDto = getAverageBookRatingPerMonth
                .getAverageBookRatingPerMonth(bookId);

        assertEquals(HttpStatus.OK, averageBookReviewByMonthResponseDto.responseStatus);
        assertEquals("Average book rating per month retrieved successfully!",
                averageBookReviewByMonthResponseDto.responseMessage);
    }

    @Test
    void it_should_return_400_when_book_id_is_less_than_one() {
        GetAveregeBookRatingPerMonth getAverageBookRatingPerMonth = new GetAveregeBookRatingPerMonth(bookReviewRepository);
        int bookId = 0;

        try {
            getAverageBookRatingPerMonth.getAverageBookRatingPerMonth(bookId);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            assertEquals("Book id cannot be less than or equal to 0", e.getMessage());
        }
    }

    @Test
    void it_should_return_500_when_unexpected_error_occurs() {
        GetAveregeBookRatingPerMonth getAverageBookRatingPerMonth = new GetAveregeBookRatingPerMonth(bookReviewRepository);
        int bookId = 1;
        when(bookReviewRepository.findAverageMonthBookReviewById(bookId)).thenThrow(new RuntimeException("Unexpected error"));

        try {
            getAverageBookRatingPerMonth.getAverageBookRatingPerMonth(bookId);
        } catch (HttpServerErrorException e) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatusCode());
            assertEquals("An unexpected error occurred, please try again later. " +
                    "If the problem persists, please contact support.", e.getMessage());
        }
    }

}
