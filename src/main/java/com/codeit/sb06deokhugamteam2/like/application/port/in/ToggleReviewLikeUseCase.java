package com.codeit.sb06deokhugamteam2.like.application.port.in;

import com.codeit.sb06deokhugamteam2.like.application.dto.response.ReviewLikeDto;

public interface ToggleReviewLikeUseCase {

    ReviewLikeDto toggleReviewLike(String path, String header);
}
