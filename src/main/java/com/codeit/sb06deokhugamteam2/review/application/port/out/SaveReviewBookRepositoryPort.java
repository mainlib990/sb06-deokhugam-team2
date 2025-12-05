package com.codeit.sb06deokhugamteam2.review.application.port.out;

import com.codeit.sb06deokhugamteam2.review.domain.ReviewBookDomain;

public interface SaveReviewBookRepositoryPort {

    void update(ReviewBookDomain book);
}
