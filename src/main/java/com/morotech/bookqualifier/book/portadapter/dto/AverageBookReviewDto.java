package com.morotech.bookqualifier.book.portadapter.dto;


public class AverageBookReviewDto {

    public Integer bookId;
    public Double averageRating;

    public AverageBookReviewDto(Integer bookId, Double averageRating) {
        this.bookId = bookId;
        this.averageRating = averageRating;
    }


    public AverageBookReviewDto() {}
}
