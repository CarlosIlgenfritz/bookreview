package com.morotech.bookqualifier.book.domain.builder;

import com.github.javafaker.Faker;
import com.morotech.bookqualifier.book.domain.BookReview;

public class BookReviewBuilder {
    private Integer bookId;
    private Integer rating;
    private String review;
    private Faker faker;

    public BookReviewBuilder() {
        faker = new Faker();
        bookId = faker.number().numberBetween(1, 10);
        rating = faker.number().numberBetween(1, 5);
        review = faker.lorem().sentence();
    }

    public BookReview create(){
        return new BookReview(bookId, rating, review);
    }
    public BookReviewBuilder setRating(Integer rating) {
        this.rating = rating;
        return this;
    }
}
