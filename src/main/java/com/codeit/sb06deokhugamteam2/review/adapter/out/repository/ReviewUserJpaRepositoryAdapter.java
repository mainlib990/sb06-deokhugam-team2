package com.codeit.sb06deokhugamteam2.review.adapter.out.repository;

import com.codeit.sb06deokhugamteam2.review.application.port.out.LoadReviewUserRepositoryPort;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static com.codeit.sb06deokhugamteam2.user.entity.QUser.user;

@Repository
public class ReviewUserJpaRepositoryAdapter implements LoadReviewUserRepositoryPort {

    @PersistenceContext
    private EntityManager em;

    private <T> JPAQuery<T> select(Expression<T> expr) {
        return new JPAQuery<>(em).select(expr);
    }

    @Override
    public boolean existsById(UUID userId) {
        Boolean existsUser = select(JPAExpressions.select(user)
                .from(user)
                .where(user.id.eq(userId))
                .exists())
                .fetchOne();

        return Boolean.TRUE.equals(existsUser);
    }
}
