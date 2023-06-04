package com.morotech.bookqualifier.book.application;

import com.morotech.bookqualifier.book.domain.repository.BookReviewRepository;
import com.morotech.bookqualifier.book.portadapter.dto.AverageBookReviewDto;
import com.morotech.bookqualifier.book.portadapter.dto.AverageBookReviewResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.ArrayList;

@Service
public class GetAverageBookReviews {

    private BookReviewRepository bookReviewRepository;

    @Autowired
    public GetAverageBookReviews(BookReviewRepository bookReviewRepository) {
        this.bookReviewRepository = bookReviewRepository;
    }

    public AverageBookReviewResponseDto getTopBooksBasedOnRating(Integer limit) {
        try {
            if (limit == null || limit <= 0) {
                return new AverageBookReviewResponseDto("Limit cannot be zero or negative", HttpStatus.BAD_REQUEST);
            }

            List<Map<String, Object>> resultFromDatabase =
                    bookReviewRepository.findAverageBookReviewAndLimitResults(limit);

            AverageBookReviewResponseDto averageBookReviewResponseDto =
                    new AverageBookReviewResponseDto("Avarege book reviews retrieved successfully!", HttpStatus.OK);
            averageBookReviewResponseDto.averageBookReviewDtoList = createDto(resultFromDatabase);

            return averageBookReviewResponseDto;
        } catch (Exception e) {
            return new AverageBookReviewResponseDto("An unexpected error occurred, please try again later. " +
                    "If the problem persists, please contact support.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<AverageBookReviewDto> createDto(List<Map<String, Object>> resultFromDatabase) {
        return resultFromDatabase.stream().map(AverageBookReviewDto -> new AverageBookReviewDto(
                (Integer) AverageBookReviewDto.get("bookId"),
                (Double) AverageBookReviewDto.get("averageRating"
        ))).toList();
    }
}
