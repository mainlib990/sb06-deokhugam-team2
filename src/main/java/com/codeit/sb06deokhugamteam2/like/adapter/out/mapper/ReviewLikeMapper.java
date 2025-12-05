package com.codeit.sb06deokhugamteam2.like.adapter.out.mapper;

import com.codeit.sb06deokhugamteam2.like.adapter.out.entity.ReviewLike;
import com.codeit.sb06deokhugamteam2.like.adapter.out.entity.ReviewLikeId;
import com.codeit.sb06deokhugamteam2.like.domain.model.ReviewLikeDomain;
import com.codeit.sb06deokhugamteam2.like.domain.model.ReviewLikeIdDomain;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class ReviewLikeMapper {

    public ReviewLikeId toEntityId(ReviewLikeIdDomain reviewLikeId) {
        return new ReviewLikeId()
                .reviewId(reviewLikeId.reviewId())
                .userId(reviewLikeId.userId());
    }

    public ReviewLikeDomain.Snapshot toDomain(ReviewLike reviewLike) {
        UUID reviewId = reviewLike.id().reviewId();
        UUID userId = reviewLike.id().userId();
        Instant likedAt = reviewLike.likedAt();

        return new ReviewLikeDomain.Snapshot(reviewId, userId, true, likedAt);
    }

    public ReviewLike toEntity(ReviewLikeDomain.Snapshot reviewLikeSnapshot) {
        ReviewLikeId reviewLikeIdEntity = new ReviewLikeId()
                .reviewId(reviewLikeSnapshot.reviewId())
                .userId(reviewLikeSnapshot.userId());

        return new ReviewLike()
                .id(reviewLikeIdEntity)
                .likedAt(reviewLikeSnapshot.likedAt());
    }
}
