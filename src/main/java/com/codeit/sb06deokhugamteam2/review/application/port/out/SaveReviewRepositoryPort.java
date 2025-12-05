package com.codeit.sb06deokhugamteam2.review.application.port.out;

import com.codeit.sb06deokhugamteam2.review.domain.ReviewDomain;

public interface SaveReviewRepositoryPort {

    void save(ReviewDomain review);

    void softDelete(ReviewDomain review);

    void hardDelete(ReviewDomain review);

    void update(ReviewDomain review);
}
