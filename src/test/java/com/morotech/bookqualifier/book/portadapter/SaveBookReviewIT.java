package com.morotech.bookqualifier.book.portadapter;

import com.morotech.bookqualifier.book.application.SaveBookReview;
import com.morotech.bookqualifier.book.domain.builder.BookReviewDtoBuilder;
import com.morotech.bookqualifier.book.domain.repository.BookReviewRepository;
import com.morotech.bookqualifier.book.portadapter.BookRest;
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
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SaveBookReviewIT {

    @LocalServerPort
    private int port;

    @Autowired
    private BookRest bookRest;

    @Autowired
    private RestTemplate restTemplate;

    @Mock
    private BookReviewRepository bookReviewRepository;

    @InjectMocks
    private SaveBookReview saveBookReview;

    @Test
    public void it_should_save_book_review() {
        cleanDatabase();
        String url = "http://localhost:" + port + "/books/save";
        BookReviewDto bookReviewDto = new BookReviewDtoBuilder().create();

        ResponseEntity<String> response = restTemplate.postForEntity(url, bookReviewDto, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Book Review saved!", response.getBody());
    }

    @Test
    public void domain_exception_should_be_thrown_when_book_review_is_not_valid() {
        cleanDatabase();
        String url = "http://localhost:" + port + "/books/save";
        BookReviewDto bookReviewDto = new BookReviewDtoBuilder().create();
        bookReviewDto.rating = -1;

        try {
            restTemplate.postForEntity(url, bookReviewDto, String.class);
        } catch (HttpClientErrorException e) {
            assertTrue(e.getMessage().contains("The rating of the book you are trying to " +
                    "review cannot be less than zero or greater than five."));
        }
    }
    private void cleanDatabase(){
        System.out.println("Cleaning the database");
        bookReviewRepository.deleteAll();
        bookReviewRepository.flush();
    }
}
