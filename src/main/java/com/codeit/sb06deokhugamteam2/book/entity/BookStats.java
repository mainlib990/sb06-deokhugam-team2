package com.codeit.sb06deokhugamteam2.book.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "BookStats")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookStats {
    @Id
    private UUID bookId;

    @Setter
    @Builder.Default    // 빌더 사용 시 기본값 설정
    @Column(nullable = false, name = "review_count")
    private int reviewCount = 0;

    @Setter
    @Builder.Default
    @Column(nullable = false, name = "rating_sum")
    private int ratingSum = 0;
}
