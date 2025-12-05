package com.codeit.sb06deokhugamteam2.like.application.port.out;

import java.util.UUID;

public interface LoadReviewLikeUserRepositoryPort {

    boolean existsById(UUID userId);
}
