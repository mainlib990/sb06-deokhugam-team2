package com.codeit.sb06deokhugamteam2.like.adapter.in;

import com.codeit.sb06deokhugamteam2.common.exception.ErrorResponse;
import com.codeit.sb06deokhugamteam2.like.domain.exception.ReviewLikeException;
import com.codeit.sb06deokhugamteam2.like.domain.exception.ReviewLikeReviewNotFoundException;
import com.codeit.sb06deokhugamteam2.like.domain.exception.ReviewLikeUserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Collections;

@RestControllerAdvice
public class ReviewLikeControllerAdvice {

    private static final Logger log = LoggerFactory.getLogger(ReviewLikeControllerAdvice.class);

    @ExceptionHandler(ReviewLikeReviewNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReviewLikeReviewNotFoundException(ReviewLikeReviewNotFoundException e) {
        log.error(e.getMessage());
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .code("REVIEW_NOT_FOUND")
                .message("리뷰를 찾을 수 없습니다.")
                .details(Collections.emptyMap())
                .exceptionType(e.getClass().getSimpleName())
                .status(HttpStatus.NOT_FOUND.value())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ReviewLikeUserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReviewLikeUserNotFoundException(ReviewLikeUserNotFoundException e) {
        log.error(e.getMessage());
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .code("USER_NOT_FOUND")
                .message("사용자를 찾을 수 없습니다.")
                .details(Collections.emptyMap())
                .exceptionType(e.getClass().getSimpleName())
                .status(HttpStatus.NOT_FOUND.value())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ReviewLikeException.class)
    public ResponseEntity<ErrorResponse> handleReviewLikeException(ReviewLikeException e) {
        log.error(e.getMessage());
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .code("REVIEW_LIKE_ERROR")
                .message("리뷰 좋아요 처리 중 오류가 발생했습니다.")
                .details(Collections.emptyMap())
                .exceptionType(e.getClass().getSuperclass().getSimpleName())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
