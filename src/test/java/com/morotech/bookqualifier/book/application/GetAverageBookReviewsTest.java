package com.morotech.bookqualifier.book.application;

import com.github.javafaker.Faker;
import com.morotech.bookqualifier.book.domain.repository.BookReviewRepository;
import com.morotech.bookqualifier.book.portadapter.dto.AverageBookReviewResponseDto;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetAverageBookReviewsTest {
    private BookReviewRepository bookReviewRepository;
    private Faker faker;

    @BeforeEach
    void init() {
        bookReviewRepository = mock(BookReviewRepository.class);
        faker  = new Faker();
    }

    @Test
    void it_should_return_200_when_everything_is_ok() {
        GetAverageBookReviews getAverageBookReviews = new GetAverageBookReviews(bookReviewRepository);
        int limit = faker.number().numberBetween(1, 10);
        when(bookReviewRepository.findAverageBookReviewAndLimitResults(limit)).thenReturn(List.of(Map.of(
                "bookId", 1, "averageRating", 1.0)));

        AverageBookReviewResponseDto topBooksBasedOnRating =
                getAverageBookReviews.getTopBooksBasedOnRating(limit);

        assertEquals(HttpStatus.OK, topBooksBasedOnRating.responseStatus);
        assertEquals("Avarege book reviews retrieved successfully!", topBooksBasedOnRating.responseMessage);

    }

    @Test
    void it_should_return_400_when_limit_is_less_than_1() {
        GetAverageBookReviews getAverageBookReviews = new GetAverageBookReviews(bookReviewRepository);
        int limit = 0;
        when(bookReviewRepository.findAverageBookReviewAndLimitResults(limit)).thenReturn(List.of(Map.of(
                "bookId", 1, "averageRating", 1.0)));
        try{
            getAverageBookReviews.getTopBooksBasedOnRating(limit);
        }catch (HttpClientErrorException e){
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }

    @Test
    void it_should_return_500_when_unexpected_error_occurs() {
        GetAverageBookReviews getAverageBookReviews = new GetAverageBookReviews(bookReviewRepository);
        int limit = 1;
        when(bookReviewRepository.findAverageBookReviewAndLimitResults(limit))
                .thenThrow(new RuntimeException("Unexpected error"));

        try{
            getAverageBookReviews.getTopBooksBasedOnRating(limit);
        }catch (HttpServerErrorException e){
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatusCode());
            assertEquals("An unexpected error occurred, please try again later. " +
                    "If the problem persists, please contact support.", e.getMessage());
        }
    }
}
