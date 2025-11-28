package com.codeit.sb06deokhugamteam2.review.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public record ReviewLikeDomain(UUID userId, LocalDateTime likedAt) {

    public boolean isLikedBy(UUID userId) {
        return Objects.equals(this.userId, userId);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ReviewLikeDomain that)) return false;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId);
    }
}
