package com.codeit.sb06deokhugamteam2.like.adapter.out.repository;

import com.codeit.sb06deokhugamteam2.like.application.port.out.LoadReviewLikeReviewRepositoryPort;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static com.codeit.sb06deokhugamteam2.review.adapter.out.entity.QReviewLike.reviewLike;

@Repository
public class ReviewLikeReviewJpaRepositoryAdapter implements LoadReviewLikeReviewRepositoryPort {

    @PersistenceContext
    private EntityManager em;

    private <T> JPAQuery<T> select(Expression<T> expr) {
        return new JPAQuery<>(em).select(expr);
    }

    @Override
    public boolean existsById(UUID reviewId) {
        Boolean existsReviewLike = select(JPAExpressions.select(reviewLike)
                .from(reviewLike)
                .where(reviewLike.review.id.eq(reviewId))
                .exists())
                .fetchOne();

        return Boolean.TRUE.equals(existsReviewLike);
    }
}
