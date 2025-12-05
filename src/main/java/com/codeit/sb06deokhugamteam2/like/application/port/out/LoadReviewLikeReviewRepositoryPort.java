package com.codeit.sb06deokhugamteam2.like.application.port.out;

import java.util.UUID;

public interface LoadReviewLikeReviewRepositoryPort {

    boolean existsById(UUID reviewId);
}
