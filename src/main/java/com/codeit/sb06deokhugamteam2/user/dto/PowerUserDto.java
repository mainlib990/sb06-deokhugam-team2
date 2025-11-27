package com.codeit.sb06deokhugamteam2.user.dto;

import java.time.LocalDateTime;
import java.util.UUID;


public record PowerUserDto(
        UUID userId,
        String nickname,
        String period,
        LocalDateTime createdAt,
        long rank,
        double score,
        double reviewScoreSum,
        long likeCount,
        long commentCount
) {
}
