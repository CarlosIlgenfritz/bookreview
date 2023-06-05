package com.morotech.bookqualifier.book.portadapter;

import com.morotech.bookqualifier.book.application.*;
import com.morotech.bookqualifier.book.portadapter.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookRest {
    private SearchBookByName searchBookByName;
    private SaveBookReview saveBookReview;
    private GetDetailedBookData getDetailedBookData;
    private GetAverageBookReviews getAverageBookReviews;

    private GetAveregeBookRatingPerMonth getAverageBookRatingPerMonth;

    @Autowired
    public BookRest(SearchBookByName searchBookByName, SaveBookReview saveBookReview, GetDetailedBookData getDetailedBookData,
                    GetAverageBookReviews getAverageBookReviews, GetAveregeBookRatingPerMonth getAverageBookRatingPerMonth) {
        this.searchBookByName = searchBookByName;
        this.saveBookReview = saveBookReview;
        this.getDetailedBookData = getDetailedBookData;
        this.getAverageBookReviews = getAverageBookReviews;
        this.getAverageBookRatingPerMonth = getAverageBookRatingPerMonth;
    }

    @GetMapping("/search")
    @Transactional
    public ResponseEntity<SearchResultDto> searchForBooks(@RequestParam String name) {
        SearchResultDto searchResultDto = searchBookByName.searchBookByName(name);
        return new ResponseEntity<>(searchResultDto, searchResultDto.httpStatus);
    }

    @PostMapping("/save")
    @Transactional
    public ResponseEntity<String> saveBookReview(@RequestBody BookReviewDto bookReviewDto) {
        return saveBookReview.save(bookReviewDto);
    }

    @GetMapping("/detailed")
    @Transactional
    public ResponseEntity<DetailedBookDataDto> searchForBooks(@RequestParam Integer id) {
        DetailedBookDataDto detailedBookDataDto = getDetailedBookData.get(id);
        return new ResponseEntity<>(detailedBookDataDto, detailedBookDataDto.httpStatus);
    }

    @GetMapping("/average")
    @Transactional(readOnly = true)
    public ResponseEntity<AverageBookReviewResponseDto> averageBookReview(@RequestParam Integer limit) {
        AverageBookReviewResponseDto averageBookReviewResponseDto = getAverageBookReviews.getTopBooksBasedOnRating(limit);
        return new ResponseEntity<>(averageBookReviewResponseDto, averageBookReviewResponseDto.responseStatus);
    }

    @GetMapping("/average/month")
    @Transactional(readOnly = true)
    public ResponseEntity<AverageBookReviewByMonthResponseDto> averageBookReviewByMonth(@RequestParam Integer bookId) {
        AverageBookReviewByMonthResponseDto averageBookReviewResponseDto = getAverageBookRatingPerMonth.getAverageBookRatingPerMonth(bookId);
        return new ResponseEntity<>(averageBookReviewResponseDto, averageBookReviewResponseDto.responseStatus);
    }
}
