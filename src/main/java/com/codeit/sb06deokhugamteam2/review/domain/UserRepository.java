package com.codeit.sb06deokhugamteam2.review.domain;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<ReviewUser> findById(UUID userId);
}
