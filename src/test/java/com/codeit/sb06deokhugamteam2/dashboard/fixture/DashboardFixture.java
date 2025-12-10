package com.codeit.sb06deokhugamteam2.dashboard.fixture;

import com.codeit.sb06deokhugamteam2.book.entity.Book;
import com.codeit.sb06deokhugamteam2.common.enums.PeriodType;
import com.codeit.sb06deokhugamteam2.common.enums.RankingType;
import com.codeit.sb06deokhugamteam2.dashboard.entity.Dashboard;
import com.codeit.sb06deokhugamteam2.review.adapter.out.entity.Review;
import com.codeit.sb06deokhugamteam2.review.adapter.out.entity.ReviewStat;
import com.codeit.sb06deokhugamteam2.user.entity.User;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class DashboardFixture {

    public static Dashboard createDashboard(int count, UUID reviewId) {

        Dashboard dashboard = Dashboard.builder()
                .rank((long) count)
                .score((double) count)
                .entityId(reviewId)
                .periodType(PeriodType.DAILY)
                .rankingType(RankingType.REVIEW)
                .build();
        return dashboard;
    }

    public static List<Dashboard> createDashboards(int number, List<UUID> reviewIds) {
        if (reviewIds.size() < number) {
            return new ArrayList<>();
        }

        return IntStream.rangeClosed(1, number)
                .mapToObj(num -> createDashboard(num, reviewIds.get(num - 1)))
                .toList();
    }

    public static User createUser(int count, Review review) {
        User user = User.builder()
                .email("test" + count + "@naver.com")
                .comments(null)
                .reviews(List.of(review))
                .nickname("nickname" + count)
                .password("password" + count)
                .build();
        review.user(user);
        return user;
    }

    public static List<User> createUsers(int number, List<Review> reviews) {
        if (reviews.size() < number) {
            return new ArrayList<>();
        }
        return IntStream.rangeClosed(1, number)
                .mapToObj(num -> createUser(num, reviews.get(num - 1)))
                .toList();
    }

    public static Review createReview(int count, Book book) {
        Review review = new Review();
        review.deleted(false);
        review.rating(count % 5);
        review.reviewStat(createReviewStat(review));
        review.content("test review" + count);
        review.book(book);
        review.id(UUID.randomUUID());
        review.createdAt(Instant.now().minus(1, ChronoUnit.DAYS));
        review.updatedAt(Instant.now().minus(1, ChronoUnit.DAYS));
        return review;
    }

    public static List<Review> createReviews(int number, List<Book> books) {
        if (books.size() < number) {
            return new ArrayList<>();
        }
        return IntStream.rangeClosed(1, number)
                .mapToObj(num -> createReview(num, books.get(num - 1))).toList();
    }

    private static ReviewStat createReviewStat(Review review) {
        ReviewStat reviewStat = new ReviewStat();
        reviewStat.review(review);
        reviewStat.commentCount(0);
        reviewStat.likeCount(0);
        return reviewStat;
    }
}
