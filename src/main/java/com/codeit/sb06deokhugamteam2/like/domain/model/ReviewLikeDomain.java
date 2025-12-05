package com.codeit.sb06deokhugamteam2.like.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ReviewLikeDomain {

    private final UUID reviewId;
    private final UUID userId;
    private boolean isLike;
    private final Instant likedAt;
    private final List<Object> events;

    public ReviewLikeDomain(UUID reviewId, UUID userId, boolean isLike, Instant likedAt) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.isLike = isLike;
        this.likedAt = likedAt;
        this.events = new ArrayList<>();
    }

    public static ReviewLikeDomain create(UUID reviewId, UUID userId) {
        boolean isLike = false;
        Instant likedAt = Instant.now();

        return new ReviewLikeDomain(reviewId, userId, isLike, likedAt);
    }

    public static ReviewLikeDomain from(Snapshot reviewLikeSnapshot) {
        UUID reviewId = reviewLikeSnapshot.reviewId();
        UUID userId = reviewLikeSnapshot.userId();
        boolean isLike = reviewLikeSnapshot.isLike();
        Instant likedAt = reviewLikeSnapshot.likedAt();

        return new ReviewLikeDomain(reviewId, userId, isLike, likedAt);
    }

    public ReviewLikeDomain toggleLike() {
        isLike = !isLike;
        return this;
    }

    public Snapshot toSnapshot() {
        return new Snapshot(reviewId, userId, isLike, likedAt);
    }

    public void clearEvents() {
        events.clear();
    }

    public List<Object> getEvents() {
        return Collections.unmodifiableList(events);
    }

    public UUID reviewId() {
        return reviewId;
    }

    public UUID userId() {
        return userId;
    }

    public boolean isLiked() {
        return isLike;
    }

    public record Snapshot(UUID reviewId, UUID userId, boolean isLike, Instant likedAt) {
    }
}
