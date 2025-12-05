package com.codeit.sb06deokhugamteam2.like.domain.exception;

import com.codeit.sb06deokhugamteam2.review.domain.exception.ReviewException;

import java.util.UUID;

public class ReviewLikeReviewNotFoundException extends ReviewException {
    
    public ReviewLikeReviewNotFoundException(UUID reviewId) {
        super("Review not found with id: '%s'".formatted(reviewId));
    }
}
