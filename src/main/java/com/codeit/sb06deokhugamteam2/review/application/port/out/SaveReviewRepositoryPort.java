package com.codeit.sb06deokhugamteam2.review.application.port.out;

import com.codeit.sb06deokhugamteam2.review.domain.model.ReviewDomain;

public interface SaveReviewRepositoryPort {

    void save(ReviewDomain.Snapshot reviewSnapshot);

    void softDelete(ReviewDomain.Snapshot reviewSnapshot);

    void hardDelete(ReviewDomain.Snapshot reviewSnapshot);

    void update(ReviewDomain.Snapshot reviewSnapshot);
}
