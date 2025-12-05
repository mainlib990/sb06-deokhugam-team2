package com.codeit.sb06deokhugamteam2.like.adapter.in;

import com.codeit.sb06deokhugamteam2.like.application.dto.response.ReviewLikeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.ResponseEntity;

@Tag(name = "리뷰 관리", description = "리뷰 관련 API")
public interface ReviewLikeApi {

    @Operation(summary = "리뷰 좋아요", description = "리뷰에 좋아를 추가하거나 취소합니다.")
    @ApiResponse(responseCode = "200", description = "리뷰 좋아요 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요 (요청자 ID 누락)")
    @ApiResponse(responseCode = "404", description = "리뷰 정보 없음")
    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    ResponseEntity<ReviewLikeDto> postReviewLike(
            @Parameter(
                    required = true,
                    schema = @Schema(format = "uuid"),
                    in = ParameterIn.PATH,
                    description = "리뷰 ID",
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @UUID(message = "리뷰 ID는 UUID 형식이어야 합니다.")
            String path,

            @Parameter(
                    required = true,
                    schema = @Schema(format = "uuid"),
                    in = ParameterIn.HEADER,
                    name = "Deokhugam-Request-User-ID",
                    description = "요청자 ID",
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @NotNull(message = "요청 사용자 ID는 필수입니다.")
            @UUID(message = "요청 사용자 ID는 UUID 형식이어야 합니다.")
            String header
    );
}
