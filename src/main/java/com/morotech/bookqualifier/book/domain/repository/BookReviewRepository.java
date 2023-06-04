package com.morotech.bookqualifier.book.domain.repository;

import com.morotech.bookqualifier.book.domain.BookReview;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public interface BookReviewRepository extends JpaRepository<BookReview, UUID> {
    List<BookReview> findByBookId(Integer bookId);

    @Query(value = "SELECT book_id as bookId, CAST(ROUND(AVG(rating), 2) as double precision) AS averageRating " +
            "FROM book_review " +
            "GROUP BY book_id " +
            "ORDER BY averageRating DESC " +
            "LIMIT ?1", nativeQuery = true)
    List<Map<String, Object>> findAverageBookReviewAndLimitResults(Integer limit);

    @Query(value = " SELECT TO_CHAR(DATE_TRUNC('month', review_date),'yyyy-MM') AS dateMonth, CAST(ROUND(AVG(rating), 2) as double precision) " +
            " AS averageRating FROM book_review br WHERE book_id = ?1 GROUP BY dateMonth ORDER BY dateMonth;", nativeQuery = true)
    List<Map<String, Object>> findAverageMonthBookReviewById(Integer bookId);
}
