package com.codeit.sb06deokhugamteam2.review.application.port.in;

import com.codeit.sb06deokhugamteam2.review.application.dto.response.ReviewDto;
import com.codeit.sb06deokhugamteam2.review.application.dto.request.ReviewUpdateRequest;

public interface UpdateReviewUseCase {

    ReviewDto updateReview(String path, String header, ReviewUpdateRequest requestBody);
}
