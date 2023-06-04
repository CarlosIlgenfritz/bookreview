package com.morotech.bookqualifier.book.application;

import com.morotech.bookqualifier.book.domain.repository.BookReviewRepository;
import com.morotech.bookqualifier.book.portadapter.dto.AverageBookReviewByMonthDto;
import com.morotech.bookqualifier.book.portadapter.dto.AverageBookReviewByMonthResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class GetAveregeBookRatingPerMonth {
    private BookReviewRepository bookReviewRepository;

    @Autowired
    public GetAveregeBookRatingPerMonth(BookReviewRepository bookReviewRepository) {
        this.bookReviewRepository = bookReviewRepository;
    }

    public AverageBookReviewByMonthResponseDto getAverageBookRatingPerMonth(Integer bookId) {
        if (bookId <= 0) {
            return new AverageBookReviewByMonthResponseDto("Book id cannot be less than or equal to 0", HttpStatus.BAD_REQUEST);
        }
        try {
            List<Map<String, Object>> averageMonthBookReviewById = bookReviewRepository.findAverageMonthBookReviewById(bookId);

            AverageBookReviewByMonthResponseDto averageBookReviewByMonthResponseDto =
                    new AverageBookReviewByMonthResponseDto("Average book rating per month retrieved successfully!",
                            HttpStatus.OK);
            averageBookReviewByMonthResponseDto.averageBookReviewByMonthDtoList = createDto(averageMonthBookReviewById);

            return averageBookReviewByMonthResponseDto;
        } catch (Exception e) {
            e.printStackTrace();
            return new AverageBookReviewByMonthResponseDto("An unexpected error occurred, please try again later. " +
                    "If the problem persists, please contact support.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<AverageBookReviewByMonthDto> createDto(List<Map<String, Object>> averageMonthBookReviewById) {
        return averageMonthBookReviewById.stream()
                .map(averageMonthBookReview -> {
                    return new AverageBookReviewByMonthDto(
                            (String) averageMonthBookReview.get("dateMonth"),
                            (Double) averageMonthBookReview.get("averageRating"));
                })
                .toList();
    }

}
