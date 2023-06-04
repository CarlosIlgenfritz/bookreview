package com.morotech.bookqualifier.book.application;

import com.github.javafaker.Faker;
import com.morotech.bookqualifier.book.domain.BookReview;
import com.morotech.bookqualifier.book.domain.builder.BookReviewBuilder;
import com.morotech.bookqualifier.book.domain.builder.GutendexSearchResultDtoBuilder;
import com.morotech.bookqualifier.book.domain.repository.BookReviewRepository;
import com.morotech.bookqualifier.book.portadapter.GutendexHttpClient;
import com.morotech.bookqualifier.book.portadapter.dto.DetailedBookDataDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.ResourceAccessException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetDetailedBookDataTest {
    private BookReviewRepository bookReviewRepository;
    private GutendexHttpClient gutendexHttpClient;
    private Faker faker;

    @BeforeEach
    void init() {
        bookReviewRepository = mock(BookReviewRepository.class);
        gutendexHttpClient = mock(GutendexHttpClient.class);
        faker = new Faker();
    }

    @Test
    void it_should_return_status_200_when_book_data_is_found() {
        Integer id = faker.number().numberBetween(0, 100);
        GetDetailedBookData getDetailedBookData = new GetDetailedBookData(bookReviewRepository, gutendexHttpClient);
        when(bookReviewRepository.findByBookId(id)).thenReturn(List.of(new BookReviewBuilder().create()));
        when(gutendexHttpClient.getBookBasedOnId(id)).thenReturn(new GutendexSearchResultDtoBuilder().create());

        DetailedBookDataDto detailedBookDataDtoResponseEntity = getDetailedBookData.get(id);

        assertEquals(HttpStatus.OK, detailedBookDataDtoResponseEntity.httpStatus);
    }

    @Test
    void it_should_calculate_average_rating_of_book_reviews() {
        Integer id = faker.number().numberBetween(0, 100);
        BookReview bookReviewOne = new BookReviewBuilder().setRating(5).create();
        BookReview bookReviewTwo = new BookReviewBuilder().setRating(4).create();
        GetDetailedBookData getDetailedBookData = new GetDetailedBookData(bookReviewRepository, gutendexHttpClient);
        when(bookReviewRepository.findByBookId(id)).thenReturn(List.of(bookReviewOne, bookReviewTwo));
        when(gutendexHttpClient.getBookBasedOnId(id)).thenReturn(new GutendexSearchResultDtoBuilder().create());

        DetailedBookDataDto detailedBookDataDtoResponseEntity = getDetailedBookData.get(id);

        assertEquals(4.5, detailedBookDataDtoResponseEntity.rating);
    }

    @Test
    void it_should_return_status_400_book_id_is_null() {
        Integer id = null;
        GetDetailedBookData getDetailedBookData = new GetDetailedBookData(bookReviewRepository, gutendexHttpClient);

        DetailedBookDataDto detailedBookDataDtoResponseEntity = getDetailedBookData.get(id);

        assertEquals(HttpStatus.BAD_REQUEST, detailedBookDataDtoResponseEntity.httpStatus);
        assertEquals("Book id is required.", detailedBookDataDtoResponseEntity.responseMessage);
    }

    @Test
    void it_should_return_status_400_book_id_is_below_zero() {
        Integer id = -1;
        GetDetailedBookData getDetailedBookData = new GetDetailedBookData(bookReviewRepository, gutendexHttpClient);

        DetailedBookDataDto detailedBookDataDtoResponseEntity = getDetailedBookData.get(id);

        assertEquals(HttpStatus.BAD_REQUEST, detailedBookDataDtoResponseEntity.httpStatus);
        assertEquals("Book id cannot be negative or zero.", detailedBookDataDtoResponseEntity.responseMessage);
    }

    @Test
    void it_should_return_status_400_book_id_is_zero() {
        Integer id = 0;
        GetDetailedBookData getDetailedBookData = new GetDetailedBookData(bookReviewRepository, gutendexHttpClient);

        DetailedBookDataDto detailedBookDataDtoResponseEntity = getDetailedBookData.get(id);

        assertEquals(HttpStatus.BAD_REQUEST, detailedBookDataDtoResponseEntity.httpStatus);
        assertEquals("Book id cannot be negative or zero.", detailedBookDataDtoResponseEntity.responseMessage);
    }

    @Test
    void it_should_return_status_404_when_book_data_is_not_found_in_gutendex() {
        Integer id = faker.number().numberBetween(0, 100);
        GetDetailedBookData getDetailedBookData = new GetDetailedBookData(bookReviewRepository, gutendexHttpClient);
        when(bookReviewRepository.findByBookId(id)).thenReturn(List.of(new BookReviewBuilder().create()));
        when(gutendexHttpClient.getBookBasedOnId(id)).thenReturn(new GutendexSearchResultDtoBuilder().createWithBooksEmpty());

        DetailedBookDataDto detailedBookDataDtoResponseEntity = getDetailedBookData.get(id);

        assertEquals(HttpStatus.NOT_FOUND, detailedBookDataDtoResponseEntity.httpStatus);
        assertEquals("Book data not found in Gutendex API.", detailedBookDataDtoResponseEntity.responseMessage);
    }

    @Test
    void it_should_return_status_408_when_gutendex_is_timeout() {
        Integer id = faker.number().numberBetween(0, 100);
        GetDetailedBookData getDetailedBookData = new GetDetailedBookData(bookReviewRepository, gutendexHttpClient);
        when(bookReviewRepository.findByBookId(id)).thenReturn(List.of(new BookReviewBuilder().create()));
        when(gutendexHttpClient.getBookBasedOnId(id)).thenThrow(new ResourceAccessException("Timeout"));

        DetailedBookDataDto detailedBookDataDtoResponseEntity = getDetailedBookData.get(id);

        assertEquals(HttpStatus.REQUEST_TIMEOUT, detailedBookDataDtoResponseEntity.httpStatus);
        assertEquals("Guntendex API is not available.", detailedBookDataDtoResponseEntity.responseMessage);
    }

    @Test
    void it_should_return_status_404_when_book_data_is_not_found_in_database() {
        Integer id = faker.number().numberBetween(0, 100);
        GetDetailedBookData getDetailedBookData = new GetDetailedBookData(bookReviewRepository, gutendexHttpClient);
        when(bookReviewRepository.findByBookId(id)).thenReturn(Collections.emptyList());
        when(gutendexHttpClient.getBookBasedOnId(id)).thenReturn(new GutendexSearchResultDtoBuilder().create());

        DetailedBookDataDto detailedBookDataDtoResponseEntity = getDetailedBookData.get(id);

        assertEquals(HttpStatus.NOT_FOUND, detailedBookDataDtoResponseEntity.httpStatus);
        assertEquals("No book review found for this book.", detailedBookDataDtoResponseEntity.responseMessage);
    }

    @Test
    void it_should_return_status_500_when_error_occurs() {
        Integer id = faker.number().numberBetween(0, 100);
        GetDetailedBookData getDetailedBookData = new GetDetailedBookData(bookReviewRepository, gutendexHttpClient);
        when(bookReviewRepository.findByBookId(id)).thenThrow(new RuntimeException("Error"));

        DetailedBookDataDto detailedBookDataDtoResponseEntity = getDetailedBookData.get(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, detailedBookDataDtoResponseEntity.httpStatus);
        assertEquals("An unexpected error occurred, please try again, if the error persists contact support",
                detailedBookDataDtoResponseEntity.responseMessage);
    }

}
