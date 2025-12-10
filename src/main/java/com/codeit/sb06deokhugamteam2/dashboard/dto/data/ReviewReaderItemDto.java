package com.codeit.sb06deokhugamteam2.dashboard.dto.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ReviewReaderItemDto {
    private UUID reviewId;
    private double score;
}
