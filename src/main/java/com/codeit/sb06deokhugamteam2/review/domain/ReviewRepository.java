package com.codeit.sb06deokhugamteam2.review.domain;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository {

    Optional<UUID> findByBookIdAndUserId(UUID bookId, UUID userId);

    void save(ReviewDomain review);
}
