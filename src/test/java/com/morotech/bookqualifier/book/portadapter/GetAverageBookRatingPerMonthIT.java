package com.morotech.bookqualifier.book.portadapter;

import com.morotech.bookqualifier.book.application.GetAveregeBookRatingPerMonth;
import com.morotech.bookqualifier.book.domain.builder.BookReviewDtoBuilder;
import com.morotech.bookqualifier.book.domain.repository.BookReviewRepository;
import com.morotech.bookqualifier.book.portadapter.BookRest;
import com.morotech.bookqualifier.book.portadapter.dto.AverageBookReviewByMonthResponseDto;
import com.morotech.bookqualifier.book.portadapter.dto.BookReviewDto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class GetAverageBookRatingPerMonthIT {
    @LocalServerPort
    private int port;

    @Autowired
    private BookRest bookRest;

    @Autowired
    private RestTemplate restTemplate;

    @Mock
    private BookReviewRepository bookReviewRepository;

    @InjectMocks
    private GetAveregeBookRatingPerMonth getAveregeBookRatingPerMonth;

    @Test
    public void it_should_get_average_book_rating_per_month() {
        cleanDatabase();
        String saveBookurl = "http://localhost:" + port + "/books/save";
        Integer bookId = 1;
        Integer rating = 3;
        BookReviewDto bookReviewDtoOne = new BookReviewDtoBuilder().setBookId(bookId).setRating(rating).create();
        String getBookReviewsUrl = "http://localhost:" + port + "/books/average/month?bookId=" + bookId;

        restTemplate.postForEntity(saveBookurl, bookReviewDtoOne, String.class);
        ResponseEntity<AverageBookReviewByMonthResponseDto> response =
                restTemplate.getForEntity(getBookReviewsUrl, AverageBookReviewByMonthResponseDto.class);

        assertEquals(rating.doubleValue(), response.getBody().averageBookReviewByMonthDtoList.get(0).averageRating);
    }

    @Test
    public void it_should_return_200_when_everything_is_ok() {
        cleanDatabase();
        String saveBookurl = "http://localhost:" + port + "/books/save";
        Integer bookId = 1;
        Integer rating = 3;
        BookReviewDto bookReviewDtoOne = new BookReviewDtoBuilder().setBookId(bookId).setRating(rating).create();
        String getBookReviewsUrl = "http://localhost:" + port + "/books/average/month?bookId=" + bookId;

        restTemplate.postForEntity(saveBookurl, bookReviewDtoOne, String.class);
        ResponseEntity<AverageBookReviewByMonthResponseDto> response =
                restTemplate.getForEntity(getBookReviewsUrl, AverageBookReviewByMonthResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Average book rating per month retrieved successfully!", response.getBody().responseMessage);
    }

    @Test
    public void it_should_return_400_when_book_id_is_equal_or_bellow_zero() {
        cleanDatabase();
        Integer bookId = 0;
        String getBookReviewsUrl = "http://localhost:" + port + "/books/average/month?bookId=" + bookId;

        try {
            restTemplate.getForEntity(getBookReviewsUrl, AverageBookReviewByMonthResponseDto.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            assertTrue(e.getMessage().contains("Book id cannot be less than or equal to 0"));
        }
    }

    @Test
    public void it_should_return_500_when_unexpected_error_occurs() {
        cleanDatabase();
        openMocks(this);
        Integer bookId = 1;
        String getBookReviewsUrl = "http://localhost:" + port + "/books/average/month?bookId=" + bookId;
        when(bookReviewRepository.findAverageMonthBookReviewById(bookId)).thenThrow(new RuntimeException("Exception"));

        try {
            restTemplate.getForEntity(getBookReviewsUrl, AverageBookReviewByMonthResponseDto.class);
        } catch (HttpServerErrorException e) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatusCode());
            assertTrue(e.getMessage().contains("An unexpected error occurred, please try again later. " +
                    "If the problem persists, please contact support."));
        }
    }

    private void cleanDatabase(){
        System.out.println("Cleaning the database");
        bookReviewRepository.deleteAll();
        bookReviewRepository.flush();
    }
}
