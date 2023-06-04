package com.morotech.bookqualifier.book.domain;

import com.morotech.bookqualifier.book.domain.common.DomainException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Entity
@Table(name = "book_review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookReview {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private Integer bookId;

    @Column
    private Integer rating;

    @Column
    private String review;

    @Column
    private LocalDate reviewDate;

    public BookReview(Integer bookId, Integer rating, String review) {
        validateRequiredFields(bookId, rating, review);
        this.bookId = bookId;
        this.rating = rating;
        this.review = review;
        this.reviewDate = LocalDate.now();
    }

    private void validateRequiredFields(Integer bookId, Integer rating, String review) {
        DomainException.whenValueIsNull(bookId, "The identifier of the book you are trying to" +
                " review cannot be empty.");
        DomainException.whenValueEqualOrBellowZero(bookId, "The identifier of the book you are trying to " +
                "review cannot be less than or equal to zero.");

        DomainException.whenValueIsNull(rating, "The rating of the book you are trying to review cannot be empty.");
        DomainException.whenValueIsBelowZeroOrAboveFive(rating, "The rating of the book you are trying to " +
                "review cannot be less than zero or greater than five.");

        DomainException.whenValueIsNull(review, "Please enter a valid value for the review field");
        DomainException.whenValueIsEmpty(review, "Please enter a valid value for the review field");
        DomainException.whenValueIsBlank(review, "Please enter a valid value for the review field");
        DomainException.whenTrue(review.length() > 255, "The length of your review cannot be greater " +
                "than 255 characters");
    }
}
