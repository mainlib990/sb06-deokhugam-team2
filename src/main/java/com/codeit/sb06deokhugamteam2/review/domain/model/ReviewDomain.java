package com.codeit.sb06deokhugamteam2.review.domain.model;

import com.codeit.sb06deokhugamteam2.review.domain.exception.ReviewPermissionDeniedException;

import java.time.Instant;
import java.util.UUID;

public class ReviewDomain {

    private final UUID id;
    private final UUID bookId;
    private final UUID userId;
    private final ReviewStatDomain reviewStat;
    private ReviewRatingDomain rating;
    private ReviewContentDomain content;
    private final Instant createdAt;
    private Instant updatedAt;

    public ReviewDomain(
            UUID id,
            UUID bookId,
            UUID userId,
            ReviewStatDomain reviewStat,
            ReviewRatingDomain rating,
            ReviewContentDomain content,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        this.reviewStat = reviewStat;
        this.rating = rating;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ReviewDomain create(UUID bookId, UUID userId, ReviewRatingDomain rating, ReviewContentDomain content) {
        UUID id = UUID.randomUUID();
        ReviewStatDomain reviewStat = ReviewStatDomain.create(id);
        Instant createdAt = Instant.now();
        Instant updatedAt = createdAt;

        return new ReviewDomain(
                id,
                bookId,
                userId,
                reviewStat,
                rating,
                content,
                createdAt,
                updatedAt
        );
    }

    public static ReviewDomain from(Snapshot reviewSnapshot) {
        UUID id = reviewSnapshot.id();
        UUID bookId = reviewSnapshot.bookId();
        UUID userId = reviewSnapshot.userId();
        ReviewStatDomain reviewStat = ReviewStatDomain.from(reviewSnapshot.reviewStatSnapshot());
        ReviewRatingDomain rating = reviewSnapshot.rating();
        ReviewContentDomain content = reviewSnapshot.content();
        Instant createdAt = reviewSnapshot.createdAt();
        Instant updatedAt = reviewSnapshot.updatedAt();

        return new ReviewDomain(
                id,
                bookId,
                userId,
                reviewStat,
                rating,
                content,
                createdAt,
                updatedAt
        );
    }

    public Snapshot toSnapshot() {
        ReviewStatDomain.Snapshot reviewStatSnapshot = this.reviewStat.toSnapshot();

        return new Snapshot(
                id,
                bookId,
                userId,
                reviewStatSnapshot,
                rating,
                content,
                createdAt,
                updatedAt
        );
    }

    public ReviewDomain verifyOwner(UUID requestUserId) {
        if (!userId.equals(requestUserId)) {
            throw new ReviewPermissionDeniedException(requestUserId);
        }
        return this;
    }

    public ReviewDomain edit(ReviewRatingDomain rating, ReviewContentDomain content) {
        this.rating = rating;
        this.content = content;
        this.updatedAt = Instant.now();
        return this;
    }

    public UUID id() {
        return id;
    }

    public UUID bookId() {
        return bookId;
    }

    public ReviewRatingDomain rating() {
        return rating;
    }

    public record Snapshot(
            UUID id,
            UUID bookId,
            UUID userId,
            ReviewStatDomain.Snapshot reviewStatSnapshot,
            ReviewRatingDomain rating,
            ReviewContentDomain content,
            Instant createdAt,
            Instant updatedAt
    ) {
    }
}
