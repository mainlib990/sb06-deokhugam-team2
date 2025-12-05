package com.codeit.sb06deokhugamteam2.like.application.port.out;

import com.codeit.sb06deokhugamteam2.like.domain.model.ReviewLikeDomain;
import com.codeit.sb06deokhugamteam2.like.domain.model.ReviewLikeIdDomain;

import java.util.Optional;

public interface SaveReviewLikeRepositoryPort {

    Optional<ReviewLikeDomain.Snapshot> findById(ReviewLikeIdDomain reviewLikeId);

    void save(ReviewLikeDomain.Snapshot reviewLikeSnapshot);

    void delete(ReviewLikeDomain.Snapshot reviewLikeSnapshot);
}
