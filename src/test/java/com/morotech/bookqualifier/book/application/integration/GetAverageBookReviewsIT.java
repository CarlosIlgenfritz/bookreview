package com.morotech.bookqualifier.book.application.integration;

import com.morotech.bookqualifier.book.application.GetAverageBookReviews;
import com.morotech.bookqualifier.book.domain.builder.BookReviewDtoBuilder;
import com.morotech.bookqualifier.book.domain.repository.BookReviewRepository;
import com.morotech.bookqualifier.book.portadapter.BookRest;
import com.morotech.bookqualifier.book.portadapter.dto.AverageBookReviewResponseDto;
import com.morotech.bookqualifier.book.portadapter.dto.BookReviewDto;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class GetAverageBookReviewsIT {
    @LocalServerPort
    private int port;

    @Autowired
    private BookRest bookRest;

    @Autowired
    private RestTemplate restTemplate;

    @Mock
    private BookReviewRepository  bookReviewRepository;

    @InjectMocks
    private GetAverageBookReviews  getAverageBookReviews;

    @Test
    public void it_should_get_average_book_reviews() {
        cleanDatabase();
        String saveBookurl = "http://localhost:" + port + "/books/save";
        Integer bookId = 1;
        Integer rating = 3;
        Integer limit = 1;
        BookReviewDto bookReviewDtoOne = new BookReviewDtoBuilder().setBookId(bookId).setRating(rating).create();
        String getBookReviewsUrl = "http://localhost:" + port + "/books/average?limit=" + limit;

        restTemplate.postForEntity(saveBookurl, bookReviewDtoOne, String.class);
        ResponseEntity<AverageBookReviewResponseDto> response =
                restTemplate.getForEntity(getBookReviewsUrl, AverageBookReviewResponseDto.class);

        assertEquals(bookId, response.getBody().averageBookReviewDtoList.get(0).bookId);
        assertEquals(rating.doubleValue(), response.getBody().averageBookReviewDtoList.get(0).averageRating);
    }

    @Test
    public void it_should_get_200_when_everything_is_ok() {
        cleanDatabase();
        String saveBookurl = "http://localhost:" + port + "/books/save";
        Integer bookId = 1;
        Integer rating = 3;
        Integer limit = 1;
        BookReviewDto bookReviewDtoOne = new BookReviewDtoBuilder().setBookId(bookId).setRating(rating).create();
        String getBookReviewsUrl = "http://localhost:" + port + "/books/average?limit=" + limit;

        restTemplate.postForEntity(saveBookurl, bookReviewDtoOne, String.class);
        ResponseEntity<AverageBookReviewResponseDto> response =
                restTemplate.getForEntity(getBookReviewsUrl, AverageBookReviewResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Avarege book reviews retrieved successfully!", response.getBody().responseMessage);
    }

    @Test
    public void it_should_get_500_when_unexpected_error_occurs() {
        cleanDatabase();
        openMocks(this);
        Integer limit = 1;
        String getBookReviewsUrl = "http://localhost:" + port + "/books/average?limit=" + limit;
        when(bookReviewRepository.findAverageBookReviewAndLimitResults(any())).thenThrow(new RuntimeException("Exception"));

        try{
            restTemplate.getForEntity(getBookReviewsUrl, AverageBookReviewResponseDto.class);
        }catch (HttpServerErrorException e){
            assertTrue(e.getMessage().contains("An unexpected error occurred, please try again later. " +
                    "If the problem persists, please contact support."));
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatusCode());
        }
    }

    private void cleanDatabase(){
        System.out.println("Cleaning the database");
        bookReviewRepository.deleteAll();
        bookReviewRepository.flush();
    }
}
