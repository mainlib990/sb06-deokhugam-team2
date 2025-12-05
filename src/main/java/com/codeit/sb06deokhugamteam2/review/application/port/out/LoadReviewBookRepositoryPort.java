package com.codeit.sb06deokhugamteam2.review.application.port.out;

import com.codeit.sb06deokhugamteam2.review.domain.model.ReviewBookDomain;

import java.util.Optional;
import java.util.UUID;

public interface LoadReviewBookRepositoryPort {

    Optional<ReviewBookDomain> findByIdForUpdate(UUID bookId);
}
