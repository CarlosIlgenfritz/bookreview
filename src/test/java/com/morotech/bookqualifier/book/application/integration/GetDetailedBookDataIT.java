package com.morotech.bookqualifier.book.application.integration;

import com.morotech.bookqualifier.book.application.GetDetailedBookData;
import com.morotech.bookqualifier.book.application.GutendexHttpClient;
import com.morotech.bookqualifier.book.domain.builder.BookReviewDtoBuilder;
import com.morotech.bookqualifier.book.domain.builder.GutendexSearchResultDtoBuilder;
import com.morotech.bookqualifier.book.domain.repository.BookReviewRepository;
import com.morotech.bookqualifier.book.portadapter.BookRest;
import com.morotech.bookqualifier.book.portadapter.dto.BookReviewDto;
import com.morotech.bookqualifier.book.portadapter.dto.DetailedBookDataDto;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class GetDetailedBookDataIT {

    @LocalServerPort
    private int port;

    @Autowired
    private BookRest bookRest;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BookReviewRepository  bookReviewRepository;

    @Mock
    private GutendexHttpClient gutendexHttpClient;

    @InjectMocks
    private GetDetailedBookData getDetailedBookData;

    @Test
    public void it_should_get_detailed_book_data() {
        cleanDatabase();
        String saveBookurl = "http://localhost:" + port + "/books/save";
        Integer bookId = 1;
        Integer rating = 3;
        BookReviewDto bookReviewDto = new BookReviewDtoBuilder().setBookId(bookId).setRating(rating).create();
        String getDetailedBookDataApiUrl = "http://localhost:" + port + "/books/detailed?id=" + bookId;

        restTemplate.postForEntity(saveBookurl, bookReviewDto, String.class);
        ResponseEntity<DetailedBookDataDto> response =
                restTemplate.getForEntity(getDetailedBookDataApiUrl, DetailedBookDataDto.class);

        assertEquals(1, response.getBody().id);
        assertEquals("Book data retrieved successfully.", response.getBody().responseMessage);
    }

    @Test
    public void it_should_return_error_message_if_book_id_is_below_zero() {
        cleanDatabase();
        Integer bookId = -1;
        String getDetailedBookDataApiUrl = "http://localhost:" + port + "/books/detailed?id=" + bookId;

        try{
            restTemplate.getForEntity(getDetailedBookDataApiUrl, DetailedBookDataDto.class);
        }catch (HttpClientErrorException e){
            assertTrue(e.getMessage().contains("Book id cannot be negative or zero."));
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }

    @Test
    public void it_should_return_error_message_if_book_id_is_zero() {
        cleanDatabase();
        Integer bookId = 0;
        String getDetailedBookDataApiUrl = "http://localhost:" + port + "/books/detailed?id=" + bookId;

        try{
            restTemplate.getForEntity(getDetailedBookDataApiUrl, DetailedBookDataDto.class);
        }catch (HttpClientErrorException e){
            assertTrue(e.getMessage().contains("Book id cannot be negative or zero."));
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }

    @Test
    public void it_should_return_error_message_if_no_book_is_found_for_given_id() {
        cleanDatabase();
        Integer bookId = 5;
        String getDetailedBookDataApiUrl = "http://localhost:" + port + "/books/detailed?id=" + bookId;

        try{
            restTemplate.getForEntity(getDetailedBookDataApiUrl, DetailedBookDataDto.class);
        }catch (HttpClientErrorException e){
            assertTrue(e.getMessage().contains("No book review found for this book."));
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    public void it_should_return_error_message_if_gutendex_api_is_unavaiable(){
        cleanDatabase();
        openMocks(this);
        String saveBookurl = "http://localhost:" + port + "/books/save";
        Integer bookId = 5;
        BookReviewDto bookReviewDto = new BookReviewDtoBuilder().setBookId(bookId).create();
        String getDetailedBookDataApiUrl = "http://localhost:" + port + "/books/detailed?id=" + bookId;
        when(gutendexHttpClient.getBookBasedOnId(any())).thenThrow(new ResourceAccessException("Simulated Exception"));

        restTemplate.postForEntity(saveBookurl, bookReviewDto, String.class);
        try{
            restTemplate.getForEntity(getDetailedBookDataApiUrl, DetailedBookDataDto.class);
        }catch (HttpClientErrorException e){
            assertTrue(e.getMessage().contains("Guntendex API is not available."));
            assertEquals(HttpStatus.REQUEST_TIMEOUT, e.getStatusCode());
        }
    }

    @Test
    public void it_should_return_error_message_when_gutendex_api_returns_null_for_given_id(){
        cleanDatabase();
        openMocks(this);
        String saveBookurl = "http://localhost:" + port + "/books/save";
        Integer bookId = 5;
        BookReviewDto bookReviewDto = new BookReviewDtoBuilder().setBookId(bookId).create();
        String getDetailedBookDataApiUrl = "http://localhost:" + port + "/books/detailed?id=" + bookId;
        when(gutendexHttpClient.getBookBasedOnId(any())).thenReturn(new GutendexSearchResultDtoBuilder().createWithBooksNull());

        restTemplate.postForEntity(saveBookurl, bookReviewDto, String.class);
        try{
            restTemplate.getForEntity(getDetailedBookDataApiUrl, DetailedBookDataDto.class);
        }catch (HttpClientErrorException e){
            assertTrue(e.getMessage().contains("Book data not found in Gutendex API."));
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    public void it_should_return_error_message_when_gutendex_api_returns_empty_list_for_given_id(){
        cleanDatabase();
        openMocks(this);
        String saveBookurl = "http://localhost:" + port + "/books/save";
        Integer bookId = 5;
        BookReviewDto bookReviewDto = new BookReviewDtoBuilder().setBookId(bookId).create();
        String getDetailedBookDataApiUrl = "http://localhost:" + port + "/books/detailed?id=" + bookId;
        when(gutendexHttpClient.getBookBasedOnId(any())).thenReturn(new GutendexSearchResultDtoBuilder().createWithBooksEmpty());

        restTemplate.postForEntity(saveBookurl, bookReviewDto, String.class);
        try{
            restTemplate.getForEntity(getDetailedBookDataApiUrl, DetailedBookDataDto.class);
        }catch (HttpClientErrorException e){
            assertTrue(e.getMessage().contains("Book data not found in Gutendex API."));
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    public void it_should_return_error_message_when_unexpected_error_occurs(){
        cleanDatabase();
        openMocks(this);
        String saveBookurl = "http://localhost:" + port + "/books/save";
        Integer bookId = 5;
        BookReviewDto bookReviewDto = new BookReviewDtoBuilder().setBookId(bookId).create();
        String getDetailedBookDataApiUrl = "http://localhost:" + port + "/books/detailed?id=" + bookId;
        when(gutendexHttpClient.getBookBasedOnId(any())).thenThrow(new RuntimeException("Simulated Exception"));

        restTemplate.postForEntity(saveBookurl, bookReviewDto, String.class);
        try{
            restTemplate.getForEntity(getDetailedBookDataApiUrl, DetailedBookDataDto.class);
        }catch (HttpServerErrorException e){
            assertTrue(e.getMessage().contains("An unexpected error occurred, please try again, " +
                    "if the error persists contact support"));
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatusCode());
        }
    }
    private void cleanDatabase(){
        System.out.println("Cleaning the database");
        bookReviewRepository.deleteAll();
        bookReviewRepository.flush();
    }
}
