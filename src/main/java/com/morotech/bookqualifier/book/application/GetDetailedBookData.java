package com.morotech.bookqualifier.book.application;

import com.morotech.bookqualifier.book.domain.BookReview;
import com.morotech.bookqualifier.book.domain.repository.BookReviewRepository;
import com.morotech.bookqualifier.book.portadapter.GutendexHttpClient;
import com.morotech.bookqualifier.book.portadapter.dto.DetailedBookDataDto;
import com.morotech.bookqualifier.book.portadapter.dto.GutendexBookDto;
import com.morotech.bookqualifier.book.portadapter.dto.GutendexSearchResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class GetDetailedBookData {

    private BookReviewRepository bookReviewRepository;
    private GutendexHttpClient gutendexHttpClient;

    @Autowired
    public GetDetailedBookData(BookReviewRepository bookReviewRepository, GutendexHttpClient gutendexHttpClient) {
        this.bookReviewRepository = bookReviewRepository;
        this.gutendexHttpClient = gutendexHttpClient;
    }

    @Cacheable(value = "detailedBook", unless = "!#result.responseMessage.equals('Book data retrieved successfully.')")
    public DetailedBookDataDto get(Integer bookId){
        if(bookId == null){
            return new DetailedBookDataDto("Book id is required.", HttpStatus.BAD_REQUEST);
        } else if(bookId <= 0){
            return new DetailedBookDataDto("Book id cannot be negative or zero.", HttpStatus.BAD_REQUEST);
        }

        try{
            List<BookReview> bookReviewList = bookReviewRepository.findByBookId(bookId);
            if(bookReviewList.isEmpty()){
                return new DetailedBookDataDto("No book review found for this book.",
                        HttpStatus.NOT_FOUND);
            }

            GutendexSearchResultDto bookBasedOnId;
            try {
                bookBasedOnId = gutendexHttpClient.getBookBasedOnId(bookId);
            }catch (ResourceAccessException e){
                e.printStackTrace();
                return new DetailedBookDataDto("Guntendex API is not available.",
                        HttpStatus.REQUEST_TIMEOUT);
            }
            if(bookBasedOnId.books == null || bookBasedOnId.books.isEmpty()){
                return new DetailedBookDataDto("Book data not found in Gutendex API.",
                        HttpStatus.NOT_FOUND);
            }

            DetailedBookDataDto detailedBookDataDto = createDetailedBookDataDto(bookReviewList, bookBasedOnId);
            detailedBookDataDto.responseMessage = "Book data retrieved successfully.";
            detailedBookDataDto.httpStatus = HttpStatus.OK;

            return detailedBookDataDto;
        }catch (Exception e){
            e.printStackTrace();
            return new DetailedBookDataDto("An unexpected error occurred, please try again, " +
                    "if the error persists contact support", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private DetailedBookDataDto createDetailedBookDataDto(List<BookReview> bookReviewList, GutendexSearchResultDto bookBasedOnId) {
        Double avgRating = bookReviewList.stream()
                .mapToInt(BookReview::getRating)
                .average()
                .orElse(0);
        BigDecimal roundedAvgRating = new BigDecimal(avgRating).setScale(2, RoundingMode.HALF_UP);


        List<String> listOfReviews = bookReviewList.stream().map(BookReview::getReview).toList();

        GutendexBookDto bookInformation = bookBasedOnId.books.get(0);
        DetailedBookDataDto detailedBookDataDto = new DetailedBookDataDto(bookInformation.id, bookInformation.title, bookInformation.authors,
                bookInformation.languages, bookInformation.download_count, roundedAvgRating.doubleValue(), listOfReviews);
        detailedBookDataDto.responseMessage = "Book data retrieved successfully.";
        return detailedBookDataDto;
    }

}
