package com.codeit.sb06deokhugamteam2.review.domain.exception;

public abstract class ReviewException extends RuntimeException {

    protected ReviewException(String message) {
        super(message);
    }
}
