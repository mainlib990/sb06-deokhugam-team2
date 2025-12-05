package com.codeit.sb06deokhugamteam2.like.domain.service;

import com.codeit.sb06deokhugamteam2.like.domain.model.ReviewLikeDomain;
import org.springframework.stereotype.Service;

@Service
public class ReviewLikeService {

    public void toggleLike(ReviewLikeDomain reviewLike) {
        reviewLike.toggleLike();
    }
}
