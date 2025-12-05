package com.codeit.sb06deokhugamteam2.review.domain.model;

import com.codeit.sb06deokhugamteam2.review.domain.exception.InvalidReviewCountException;

public record ReviewCountDomain(int value) {

    public ReviewCountDomain {
        if (value < 0) {
            throw new InvalidReviewCountException(value);
        }
    }

    public ReviewCountDomain(Integer value) {
        this(value.intValue());
    }
}
