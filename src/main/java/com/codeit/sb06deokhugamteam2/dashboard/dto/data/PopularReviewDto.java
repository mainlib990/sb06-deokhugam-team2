package com.codeit.sb06deokhugamteam2.dashboard.dto.data;

import com.codeit.sb06deokhugamteam2.common.enums.PeriodType;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class PopularReviewDto {
    private UUID id;
    private UUID reviewId;
    private UUID bookId;
    private String bookTitle;
    private String bookThumbnailUrl;
    private UUID userId;
    private String userNickname;
    private String reviewContent;
    private Integer reviewRating;
    private PeriodType periodType;
    private Instant createdAt;
    private long rank;
    private double score;
    private long likeCount;
    private long commentCount;
}