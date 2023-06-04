package com.morotech.bookqualifier.book.portadapter.dto;

import java.time.LocalDate;

public class AverageBookReviewByMonthDto {

    public String  date;
    public Double averageRating;

    public AverageBookReviewByMonthDto(String date, Double averageRating) {
        this.date = date;
        this.averageRating = averageRating;
    }

    public AverageBookReviewByMonthDto() {}
}
